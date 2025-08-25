package com.backend.tryal.stripe.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.stripe.dto.StripePriceRequestDTO;
import com.backend.tryal.stripe.dto.StripeProductRequestDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class StripeAdminServiceImpl implements StripeAdminService{

    private final PlanRepository planRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public StripeAdminServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    @Transactional
    public void syncPlansFromStripe() throws StripeException {
        PriceListParams params = PriceListParams.builder().setActive(true).setLimit(50L).build();
        PriceCollection prices = Price.list(params);

        Set<String> activePriceIds = new HashSet<>();

        for(Price price : prices.getData()){
            String priceId = price.getId();
            String productId = price.getProduct();

            activePriceIds.add(priceId);

            Product stripeProduct = Product.retrieve(productId);

            Optional<Plan> existingPlanOpt = planRepository.findByStripePriceId(priceId);

            if (existingPlanOpt.isPresent()) {
                Plan plan = existingPlanOpt.get();
                boolean updatedFlag = false;
                double newPrice = price.getUnitAmount() / 100.0;

                if (!Objects.equals(plan.getPrice(), newPrice)) {
                    plan.setPrice(newPrice);
                    updatedFlag = true;
                }

                if (!Objects.equals(plan.getName(), stripeProduct.getName())) {
                    plan.setName(stripeProduct.getName());
                    updatedFlag = true;
                }

                if (!Objects.equals(plan.getDescription(), stripeProduct.getDescription())) {
                    plan.setDescription(stripeProduct.getDescription());
                    updatedFlag = true;
                }

                if (!Boolean.TRUE.equals(plan.getIsActive())) {
                    plan.setIsActive(true);
                    updatedFlag = true;
                }

                if (updatedFlag) {
                    planRepository.save(plan);
                }
            } else {
                Plan newPlan = new Plan();
                newPlan.setStripePriceId(priceId);
                newPlan.setStripeProductId(productId);
                newPlan.setName(stripeProduct.getName());
                newPlan.setDescription(stripeProduct.getDescription());
                newPlan.setPrice(price.getUnitAmount() / 100.0);
                newPlan.setIsActive(true);

                // TODO: Set metadata in products or prices for credits + rollover
                newPlan.setCredits(10);
                newPlan.setRolloverCreditsAllowed(false);

                if ("recurring".equals(price.getType())) {
                    if(price.getRecurring().getInterval().equals("month")){
                        newPlan.setPlanType(Plan.PlanType.MONTH);
                    }else if(price.getRecurring().getInterval().equals("year")){
                        newPlan.setPlanType(Plan.PlanType.YEAR);
                    }
                } else {
                    newPlan.setPlanType(Plan.PlanType.ONE_TIME);
                }

                planRepository.save(newPlan);
            }
        }

        planRepository.markInactivePlansNotIn(activePriceIds);
    }

    @Override
    public Product createProduct(StripeProductRequestDTO stripeProductRequestDTO) throws StripeException {
        ProductCreateParams params = ProductCreateParams.builder()
                .setName(stripeProductRequestDTO.getName())
                .setActive(stripeProductRequestDTO.getIsActive())
                .setDescription(stripeProductRequestDTO.getDescription())
                .setTaxCode(stripeProductRequestDTO.getTaxCode())
                .build();

        Product product = Product.create(params);
        return product;
    }

    @Override
    public Price createPrice(String productId, StripePriceRequestDTO stripePriceRequestDTO) throws StripeException {
        PriceCreateParams params = null;

        PriceCreateParams.TaxBehavior taxBehavior = switch (stripePriceRequestDTO.getTaxBehavior()) {
            case EXCLUSIVE -> PriceCreateParams.TaxBehavior.EXCLUSIVE;
            case INCLUSIVE -> PriceCreateParams.TaxBehavior.INCLUSIVE;
            case UNSPECIFIED -> PriceCreateParams.TaxBehavior.UNSPECIFIED;
        };

        if(stripePriceRequestDTO.getPlanType() == Plan.PlanType.ONE_TIME){
            params = PriceCreateParams.builder()
                    .setCurrency(stripePriceRequestDTO.getCurrency().toString())
                    .setUnitAmount(stripePriceRequestDTO.getPrice())
                    .setActive(stripePriceRequestDTO.getIsActive())
                    .setProduct(productId)
                    .setTaxBehavior(taxBehavior)
                    .putMetadata("credits", stripePriceRequestDTO.getCredits().toString())
                    .putMetadata("rollover_credits_allowed", stripePriceRequestDTO.getRolloverCreditsAllowed().toString())
                    .build();
        }else if(stripePriceRequestDTO.getPlanType() == Plan.PlanType.MONTH){
            params = PriceCreateParams.builder()
                    .setCurrency(stripePriceRequestDTO.getCurrency().toString())
                    .setUnitAmount(stripePriceRequestDTO.getPrice())
                    .setRecurring(
                            PriceCreateParams.Recurring.builder()
                                    .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                    .setIntervalCount(1L)
                                    .build()
                    )
                    .setActive(stripePriceRequestDTO.getIsActive())
                    .setProduct(productId)
                    .setTaxBehavior(taxBehavior)
                    .putMetadata("credits", stripePriceRequestDTO.getCredits().toString())
                    .putMetadata("rollover_credits_allowed", stripePriceRequestDTO.getRolloverCreditsAllowed().toString())
                    .build();
        }else if(stripePriceRequestDTO.getPlanType() == Plan.PlanType.YEAR){
            params = PriceCreateParams.builder()
                    .setCurrency(stripePriceRequestDTO.getCurrency().toString())
                    .setUnitAmount(stripePriceRequestDTO.getPrice())
                    .setRecurring(
                            PriceCreateParams.Recurring.builder()
                                    .setInterval(PriceCreateParams.Recurring.Interval.YEAR)
                                    .setIntervalCount(1L)
                                    .build()
                    )
                    .setActive(stripePriceRequestDTO.getIsActive())
                    .setProduct(productId)
                    .setTaxBehavior(taxBehavior)
                    .putMetadata("credits", stripePriceRequestDTO.getCredits().toString())
                    .putMetadata("rollover_credits_allowed", stripePriceRequestDTO.getRolloverCreditsAllowed().toString())
                    .build();
        }

        Price price = Price.create(params);
        return price;
    }

    @Override
    public Price deactivatePrice(String priceId) throws StripeException {
        Plan plan = planRepository.findByStripePriceId(priceId).orElse(null);

        if(plan == null){
            throw new EntityNotFoundException("Stripe price id not found.");
        }

        plan.setIsActive(false);
        planRepository.save(plan);

        Price resource = Price.retrieve(priceId);

        PriceUpdateParams params =
                PriceUpdateParams.builder().setActive(false).build();

        Price price = resource.update(params);
        return price;
    }
}

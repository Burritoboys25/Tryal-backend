package com.backend.tryal.stripe.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.Product;
import com.stripe.param.PriceListParams;
import jakarta.annotation.PostConstruct;
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
                    newPlan.setPlanType(Plan.PlanType.SUBSCRIPTION);
                } else {
                    newPlan.setPlanType(Plan.PlanType.ONE_TIME);
                }

                planRepository.save(newPlan);
            }
        }

        planRepository.markInactivePlansNotIn(activePriceIds);
    }
}

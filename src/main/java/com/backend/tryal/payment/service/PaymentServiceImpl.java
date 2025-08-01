package com.backend.tryal.payment.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.shared.utils.TimeWizard;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public PaymentServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository, PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    @Override
    public void handleInvoicePaid(Invoice invoice) {
        System.out.println("INVOICE PAYMENT SUCCEEDED");

        String subscriptionId;
        try {
            subscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Subscription ID not found in invoice");
        }

        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID is null in invoice");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        if (subscription.getSubscriptionStatus() == Subscription.SubscriptionStatus.ACTIVE) {
            User user = subscription.getUser();
            Plan plan = subscription.getPlan();

            if (user == null) {
                throw new EntityNotFoundException("User linked to subscription not found");
            }
            if (plan == null) {
                throw new EntityNotFoundException("Plan linked to subscription not found");
            }

            int creditsToAdd = plan.getMonthlyCredits();
            int currentCredits = user.getCreditBalance() != null ? user.getCreditBalance() : 0;

            user.setCreditBalance(currentCredits + creditsToAdd);
            userRepository.save(user);
        }
    }

    @Override
    public void handleInvoiceFailed(Invoice invoice) {
        System.out.println("INVOICE PAYMENT FAILED");

        String subscriptionId;
        try {
            subscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Subscription ID not found in invoice");
        }

        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID is null in invoice");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

        User user = subscription.getUser();
        if (user != null) {
            System.out.printf("Notifying user of failed payment: ", user.getEmail());
        }
    }

    @Override
    public void handleSubscriptionDeleted(com.stripe.model.Subscription stripeSubscription) {
        System.out.println("STRIPE SUBSCRIPTION DELETED");

        String subscriptionId = stripeSubscription.getId();
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID not found in subscription object.");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);

        Long cancelAtTimestamp = stripeSubscription.getCancelAt();
        LocalDateTime endAt;

        if (cancelAtTimestamp != null) {
            endAt = LocalDateTime.ofEpochSecond(cancelAtTimestamp, 0, ZoneOffset.UTC);
        } else {
            endAt = LocalDateTime.now();
        }

        subscription.setEndAt(endAt);
        subscriptionRepository.save(subscription);

        User user = subscription.getUser();
        if (user != null) {
            System.out.printf("Notifying user of subscription cancellation: ", user.getEmail());
        }
    }

    @Override
    public void handleSubscriptionUpdated(com.stripe.model.Subscription stripeSubscription) {
        System.out.println("STRIPE SUBSCRIPTION UPDATED");

        String subscriptionId = stripeSubscription.getId();

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        if (stripeSubscription.getItems() != null
                && stripeSubscription.getItems().getData() != null
                && !stripeSubscription.getItems().getData().isEmpty()) {

            com.stripe.model.SubscriptionItem item = stripeSubscription.getItems().getData().get(0);
            String newPlanPriceId = item.getPrice().getId();

            Plan newPlan = planRepository.findByStripePriceId(newPlanPriceId)
                    .orElseThrow(() -> new EntityNotFoundException("Plan not found for Stripe price ID: " + newPlanPriceId));

            subscription.setPlan(newPlan);
        }

        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.valueOf(stripeSubscription.getStatus().toUpperCase()));

        subscriptionRepository.save(subscription);
    }

    @Override
    public void handleCheckoutCompleted(Session session) throws StripeException {
        System.out.println("STRIPE CHECKOUT: " + session);

        try {
            String userId = session.getMetadata().get("userId");
            String planId = session.getMetadata().get("planId");
            String customerId = session.getCustomer();
            String subscriptionId = session.getSubscription();

            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

            user.setStripeCustomerId(customerId);
            userRepository.save(user);

            Plan plan = planRepository.findById(UUID.fromString(planId))
                    .orElseThrow(() -> new EntityNotFoundException("Plan with id " + planId + " not found"));

            com.stripe.model.Subscription stripeSubscription = com.stripe.model.Subscription.retrieve(subscriptionId);

            Subscription newSubscription = new Subscription();
            newSubscription.setUser(user);
            newSubscription.setPlan(plan);
            newSubscription.setSubscriptionId(subscriptionId);
            Subscription.SubscriptionStatus status =
                    Subscription.SubscriptionStatus.valueOf(stripeSubscription.getStatus().toUpperCase());
            newSubscription.setSubscriptionStatus(status);
            newSubscription.setStartAt(TimeWizard.timeSpellconvert(stripeSubscription.getStartDate()));

            subscriptionRepository.save(newSubscription);

        } catch (StripeException e) {
            throw e;
        }
    }
}

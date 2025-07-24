package com.backend.tryal.payment.service;

import com.backend.tryal.plan.Plan;
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

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public PaymentServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void handleInvoicePaid(Invoice invoice) {
        System.out.println("INVOICE INVOICE INVOICE");

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
    public void handleCheckoutCompleted(Session session) throws StripeException {
        try {
            String userId = session.getMetadata().get("userId");
            String customerId = session.getCustomer();
            String subscriptionId = session.getSubscription();

            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

            user.setStripeCustomerId(customerId);
            userRepository.save(user);

            com.stripe.model.Subscription stripeSubscription = com.stripe.model.Subscription.retrieve(subscriptionId);

            Subscription newSubscription = new Subscription();
            newSubscription.setUser(user);
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

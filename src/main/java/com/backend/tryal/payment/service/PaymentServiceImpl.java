package com.backend.tryal.payment.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.plan.service.PlanService;
import com.backend.tryal.shared.utils.TimeWizard;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PaymentServiceImpl implements PaymentService{
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final PlanService planService;

    private final String domain = "http://localhost:3000";

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public PaymentServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository, PlanRepository planRepository, PlanService planService) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.planService = planService;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    private void handleInvoicePaid(Invoice invoice) {
        System.out.println("INVOICE PAYMENT SUCCEEDED");

        String planIdStr = invoice.getMetadata() != null ? invoice.getMetadata().get("planId") : null;
        String userIdStr = invoice.getMetadata() != null ? invoice.getMetadata().get("userId") : null;

        // Stripe checkouts will always contain planId and userId
        if (planIdStr != null && userIdStr != null) {
            handleCheckoutInvoice(planIdStr, userIdStr);
            return;
        }

        // Invoices paid without a planId and userId will be a subscription
        String subscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();
        if (subscriptionId != null) {
            handleSubscriptionInvoice(subscriptionId);
            return;
        }

        System.out.println("Invoice payment event missing planId or subscriptionId");
    }

    private void handleCheckoutInvoice(String planIdStr, String userIdStr) {
        UUID planId = UUID.fromString(planIdStr);
        UUID userId = UUID.fromString(userIdStr);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for id: " + planId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userId));

        addCreditsToUser(user, plan.getCredits());
    }

    private void handleSubscriptionInvoice(String subscriptionId) {
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

            addCreditsToUser(user, plan.getCredits());
            userRepository.save(user);
            return;
        }

        System.out.println("Subscription is not active for id: " + subscriptionId);
    }

    private void addCreditsToUser(User user, int creditsToAdd) {
        int currentCredits = user.getCreditBalance() != null ? user.getCreditBalance() : 0;
        user.setCreditBalance(currentCredits + creditsToAdd);
        userRepository.save(user);

        System.out.println("Credits added to user with id: " + user.getUserId());
    }

    private void handleInvoiceFailed(Invoice invoice) {
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

    private void handleSubscriptionDeleted(com.stripe.model.Subscription stripeSubscription) {
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

    private void handleSubscriptionUpdated(com.stripe.model.Subscription stripeSubscription) {
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

    private void handleCheckoutCompleted(Session session) throws StripeException {
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

            // Plan during checkout is a subscription
            if(plan.getPlanType() == Plan.PlanType.SUBSCRIPTION){
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
            }
        } catch (StripeException e) {
            throw e;
        }
    }

    @Override
    public String createCheckoutSession(String userId, String userEmail, String planId) {
        try {
            Plan plan = planService.getPlanById(UUID.fromString(planId));
            if (plan == null || plan.getStripePriceId() == null) {
                throw new IllegalArgumentException("Invalid plan");
            }

            SessionCreateParams params = SessionCreateParams.builder()
                    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setReturnUrl(domain + "/stripe/return?session_id={CHECKOUT_SESSION_ID}")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice(plan.getStripePriceId())
                                    .build()
                    )
                    .putMetadata("userId", userId)
                    .putMetadata("planId", planId)
                    .build();

            Session session = Session.create(params);
            return session.getClientSecret();

        } catch (Exception e) {
            System.out.println("Failed to create checkout session");
            throw new RuntimeException("Failed to create checkout session", e);
        }
    }

    @Override
    public Map<String, String> getSessionStatus(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            Map<String, String> response = new HashMap<>();
            response.put("status", session.getStatus());

            if (session.getCustomerDetails() != null && session.getCustomerDetails().getEmail() != null) {
                response.put("customer_email", session.getCustomerDetails().getEmail());
            } else {
                response.put("customer_email", "unknown");
            }

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve session status");
        }
    }

    @Override
    @Transactional
    public void processStripeEvent(String payload, String sigHeader) throws StripeException {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = deserializer.getObject().orElseThrow(
                () -> new IllegalArgumentException("Failed to deserialize Stripe object")
        );

        logReceivedEvent(event);

        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) stripeObject;
                handleCheckoutCompleted(session);
                break;
            case "invoice.payment_succeeded":
                Invoice invoice = (Invoice) stripeObject;
                handleInvoicePaid(invoice);
                break;
            case "invoice.payment_failed":
                Invoice failedInvoice = (Invoice) stripeObject;
                handleInvoiceFailed(failedInvoice);
                break;
            case "customer.subscription.updated":
                com.stripe.model.Subscription updatedSubscription = (com.stripe.model.Subscription) stripeObject;
                handleSubscriptionUpdated(updatedSubscription);
                break;
            case "customer.subscription.deleted":
                com.stripe.model.Subscription deletedSubscription = (com.stripe.model.Subscription) stripeObject;
                handleSubscriptionDeleted(deletedSubscription);
                break;
            case "customer.subscription.created":
                System.out.println("Subscription created");
                // TODO: Optionally log or store subscription metadata
                break;
            case "customer.created":
                System.out.println("Customer created");
                // TODO: Save customer ID if user record exists but ID is not saved yet
                break;
            case "customer.updated":
                System.out.println("Customer updated");
                // TODO: Sync billing info (optional)
                break;
            default:
                System.out.println("Unhandled event: " + event.getType());
        }
    }

    private void logReceivedEvent(Event event) {
        System.out.printf("Received Stripe event: id=%s, type=%s, created=%d%n",
                event.getId(),
                event.getType(),
                event.getCreated());
    }
}

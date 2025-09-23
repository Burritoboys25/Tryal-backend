package com.backend.tryal.stripe.service;

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
import com.stripe.param.SubscriptionUpdateParams;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.*;

import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StripeServiceImpl implements StripeService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final PlanService planService;

    private final String domain = "http://localhost:3000";

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public StripeServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository, PlanRepository planRepository, PlanService planService) {
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

        String planIdStr = invoice.getLines().getData().get(0).getMetadata().get("planId");
        String userIdStr = invoice.getLines().getData().get(0).getMetadata().get("userId");
        String stripeSubscriptionId = invoice.getLines().getData().get(0).getParent().getSubscriptionItemDetails().getSubscription();

        if(planIdStr == null || planIdStr.isBlank() || userIdStr == null || userIdStr.isBlank()){
            throw new IllegalStateException("Succeeded invoice payment event missing planId or userId");
        }

        if(stripeSubscriptionId != null && !stripeSubscriptionId.isBlank()){
            // Subscription flow
            Long startDate = invoice.getLines().getData().get(0).getPeriod().getStart();
            Long endDate = invoice.getLines().getData().get(0).getPeriod().getEnd();
            handleSubscriptionInvoice(userIdStr, startDate, endDate);
        }else{
            // Top-off flow
            handleCheckoutInvoice(planIdStr, userIdStr);
        }
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

    private void handleSubscriptionInvoice(String userIdStr, Long startDate, Long endDate) {
        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active subscription not found for userId: " + userIdStr));

        // promote pending subscription and demote current subscription
        if(!subscription.getAutoRenew()){
            Subscription pendingSubscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.PENDING)
                    .orElseThrow(() -> new EntityNotFoundException("Pending subscription not found for userId: " + userIdStr));

            subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.CANCELLED);
            pendingSubscription.setSubscriptionStatus(Subscription.SubscriptionStatus.ACTIVE);

            subscriptionRepository.saveAll(List.of(subscription, pendingSubscription));
            subscription = pendingSubscription;
        }

        subscription.setStartAt(TimeWizard.timeSpellconvert(startDate));
        subscription.setEndAt(TimeWizard.timeSpellconvert(endDate));
        subscriptionRepository.save(subscription);

        User user = subscription.getUser();
        if (user == null) {
            throw new IllegalStateException("Subscription " + subscription.getSubscriptionId() + " has no linked user");
        }

        Plan plan = subscription.getPlan();
        if (plan == null) {
            throw new IllegalStateException("Subscription " + subscription.getSubscriptionId() + " has no linked plan");
        }

        Integer credits = plan.getCredits();
        if (credits == null || credits <= 0) {
            throw new IllegalStateException("Plan for subscription " + subscription.getSubscriptionId() + " has invalid credits: " + credits);
        }

        addCreditsToUser(user, credits);
    }

    private void addCreditsToUser(User user, int creditsToAdd) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null when adding credits");
        }

        if (creditsToAdd <= 0) {
            throw new IllegalArgumentException("Credits to add must be greater than 0, received: " + creditsToAdd);
        }

        Integer currentCredits = user.getCreditBalance();
        int newBalance = (currentCredits != null ? currentCredits : 0) + creditsToAdd;

        user.setCreditBalance(newBalance);
        userRepository.save(user);

        System.out.println("Credits added to user with id: " + user.getUserId() + ". New balance: " + newBalance);
    }

    private void handleInvoiceFailed(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Failed invoice must not be null");
        }
        if (invoice.getParent() == null || invoice.getParent().getSubscriptionDetails() == null) {
            throw new IllegalArgumentException("Failed invoice is missing subscription details");
        }

        String stripeSubscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();
        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalArgumentException("Failed invoice is missing stripeSubscriptionId");
        }

        String userIdStr = invoice.getLines().getData().get(0).getMetadata().get("userId");

        if(userIdStr == null || userIdStr.isBlank()){
            throw new IllegalStateException("Failed invoice payment event missing userId");
        }

        User user = userRepository.findById(UUID.fromString(userIdStr))
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userIdStr));

        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active subscription not found for userId: " + userIdStr));

        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

        if (user.getEmail() != null) {
            System.out.println("Notifying user of failed payment: " + user.getEmail());
        }
    }

    private void handleSubscriptionDeleted(com.stripe.model.Subscription stripeSubscription) {
        if (stripeSubscription == null) {
            throw new IllegalArgumentException("Stripe subscription must not be null");
        }

        String stripeSubscriptionId = stripeSubscription.getId();

        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalArgumentException("Stripe subscription ID not found in object");
        }

        String userIdStr = stripeSubscription.getMetadata().get("userId");

        User user = userRepository.findById(UUID.fromString(userIdStr))
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userIdStr));

        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active subscription not found for userId: " + userIdStr));

        //TODO: User should still have access until the end of the billing cycle, however their subscription status will currently display CANCELLED
        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);

        subscriptionRepository.save(subscription);

        if (user.getEmail() != null) {
            System.out.println("Notifying user of subscription cancellation: " + user.getEmail());
        }
    }


    private void handleSubscriptionUpdated(com.stripe.model.Subscription stripeSubscription) {
        if (stripeSubscription == null) {
            throw new IllegalArgumentException("Stripe subscription must not be null");
        }

        String userIdStr = stripeSubscription.getMetadata().get("userId");
        String planIdStr = stripeSubscription.getMetadata().get("planId");
        String stripeSubscriptionId = stripeSubscription.getId();

        if (userIdStr == null || userIdStr.isBlank()) {
            throw new IllegalArgumentException("Stripe subscription is missing userId");
        }else if (planIdStr == null || planIdStr.isBlank()) {
            throw new IllegalArgumentException("Stripe subscription is missing planId");
        }

        Subscription currentSubscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active subscription not found for userId: " + userIdStr));

        // update subscription flow
        if(!planIdStr.equals(currentSubscription.getPlan().getPlanId())){
            if (stripeSubscription.getItems() == null || stripeSubscription.getItems().getData().isEmpty()) {
                throw new IllegalStateException("Stripe subscription items are missing for subscription: " + stripeSubscriptionId);
            }

            Plan plan = planRepository.findById(UUID.fromString(planIdStr))
                    .orElseThrow(() -> new EntityNotFoundException("Plan with id " + planIdStr + " not found"));

            Optional<Subscription> pendingSubscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.PENDING);

            if(pendingSubscription.isPresent()){
                // update existing pending subscription
                Subscription updatedSubscription = pendingSubscription.get();
                updatedSubscription.setPlan(plan);

                subscriptionRepository.save(updatedSubscription);
            }else{
                // create new pending subscription
                User user = userRepository.findById(UUID.fromString(userIdStr))
                        .orElseThrow(() -> new EntityNotFoundException("User with id " + userIdStr + " not found"));

                Subscription newSubscription = new Subscription();
                newSubscription.setUser(user);
                newSubscription.setPlan(plan);
                newSubscription.setStripeSubscriptionId(stripeSubscriptionId);
                newSubscription.setSubscriptionStatus(Subscription.SubscriptionStatus.PENDING);
                newSubscription.setStartAt(currentSubscription.getEndAt());
                newSubscription.setAutoRenew(true);
                subscriptionRepository.save(newSubscription);

                currentSubscription.setAutoRenew(false);
                subscriptionRepository.save(currentSubscription);
            }
        }else{
            // flow for other updates (ex. billing)
        }
    }

    private void handleCheckoutCompleted(Session session) throws StripeException {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null");
        }

        String userIdStr = session.getMetadata().get("userId");
        String planIdStr = session.getMetadata().get("planId");
        String customerId = session.getCustomer();

        if (userIdStr == null || userIdStr.isBlank()) {
            throw new IllegalArgumentException("Session is missing userId");
        }else if (planIdStr == null || planIdStr.isBlank()) {
            throw new IllegalArgumentException("Session is missing planId");
        }else if (customerId == null || customerId.isBlank()) {
            throw new IllegalStateException("Session is missing customerId");
        }
    }

    private void handleSubscriptionCreated(com.stripe.model.Subscription stripeSubscription) throws StripeException {
        if (stripeSubscription == null) {
            throw new IllegalArgumentException("Stripe subscription must not be null");
        }

        String userIdStr = stripeSubscription.getMetadata().get("userId");
        String planIdStr = stripeSubscription.getMetadata().get("planId");
        String customerId = stripeSubscription.getCustomer();
        String stripeSubscriptionId = stripeSubscription.getId();

        if (userIdStr == null || userIdStr.isBlank()) {
            throw new IllegalArgumentException("Stripe subscription is missing userId");
        }else if (planIdStr == null || planIdStr.isBlank()) {
            throw new IllegalArgumentException("Stripe subscription is missing planId");
        }else if (customerId == null || customerId.isBlank()) {
            throw new IllegalStateException("Stripe subscription is missing customerId");
        }

        User user = userRepository.findById(UUID.fromString(userIdStr))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userIdStr + " not found"));

        Plan plan = planRepository.findById(UUID.fromString(planIdStr))
                .orElseThrow(() -> new EntityNotFoundException("Plan with id " + planIdStr + " not found"));

        user.setStripeCustomerId(customerId);
        userRepository.save(user);

        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.ACTIVE);

        if(existingSubscription.isPresent()){
            throw new IllegalStateException("Cannot create a new subscription: user already has an active subscription.");
        }

        Subscription newSubscription = new Subscription();
        newSubscription.setUser(user);
        newSubscription.setPlan(plan);
        newSubscription.setStripeSubscriptionId(stripeSubscriptionId);

        Subscription.SubscriptionStatus status = Subscription.SubscriptionStatus.valueOf(stripeSubscription.getStatus().toUpperCase());
        newSubscription.setSubscriptionStatus(status);
        newSubscription.setAutoRenew(true);

        subscriptionRepository.save(newSubscription);
    }

    @Override
    public String createCheckoutSession(String userId, String userEmail, String planId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID must not be null or blank");
        }
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Plan ID must not be null or blank");
        }

        Plan plan = planService.getPlanById(UUID.fromString(planId));
        if (plan == null || plan.getStripePriceId() == null) {
            throw new IllegalArgumentException("Invalid plan: " + planId);
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

        try {
            Session session = Session.create(params);
            return session.getClientSecret();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create checkout session for user: " + userId, e);
        }
    }

    @Override
    public void updateStripeSubscription(String userIdStr, String priceId, String planIdStr) throws StripeException {
        User user = userRepository.findById(UUID.fromString(userIdStr))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userIdStr + " not found"));

        Plan plan = planRepository.findById(UUID.fromString(planIdStr))
                .orElseThrow(() -> new EntityNotFoundException("Plan with id " + planIdStr + " not found"));

        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(UUID.fromString(userIdStr), Subscription.SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active subscription not found for userId: " + userIdStr));

        com.stripe.model.Subscription currentStripeSub = com.stripe.model.Subscription.retrieve(subscription.getStripeSubscriptionId());

        if (currentStripeSub.getItems() == null || currentStripeSub.getItems().getData().isEmpty()) {
            throw new IllegalStateException("Subscription has no items to update");
        }

        String currentItemId = currentStripeSub.getItems().getData().get(0).getId();

        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.NONE)
                .addItem(
                        SubscriptionUpdateParams.Item.builder()
                                .setId(currentItemId)
                                .setPrice(priceId)
                                .build()
                )
                .putMetadata("planId", planIdStr)
                .build();

        currentStripeSub.update(params);
    }

    @Override
    public Map<String, String> getSessionStatus(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Session ID must not be null or blank");
        }

        Session session;
        try {
            session = Session.retrieve(sessionId);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to retrieve session for id: " + sessionId, e);
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", session.getStatus());

        String email = (session.getCustomerDetails() != null) ? session.getCustomerDetails().getEmail() : null;

        response.put("customer_email", (email != null ? email : "unknown"));
        return response;
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
                com.stripe.model.Subscription createdSubscription = (com.stripe.model.Subscription) stripeObject;
                handleSubscriptionCreated(createdSubscription);
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

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

        String planIdStr = invoice.getMetadata() != null ? invoice.getMetadata().get("planId") : null;
        String userIdStr = invoice.getMetadata() != null ? invoice.getMetadata().get("userId") : null;

        // Stripe checkouts will always contain planId and userId
        if (planIdStr != null && userIdStr != null) {
            handleCheckoutInvoice(planIdStr, userIdStr);
            return;
        }

        // Invoices paid without a planId and userId will be a subscription
        String stripeSubscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();

        if (stripeSubscriptionId != null) {
            String endDate = invoice.getParent().getSubscriptionDetails().getSubscriptionObject().get
            handleSubscriptionInvoice(stripeSubscriptionId);
            return;
        }

        throw new IllegalStateException("Invoice payment event missing planId or subscriptionId");
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

    private void handleSubscriptionInvoice(String stripeSubscriptionId, ) {
        //TODO: find in repository by stripeSubscriptionId, find current active subscription id
        Subscription subscription = subscriptionRepository.findById(stripeSubscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + stripeSubscriptionId));

        if (subscription.getSubscriptionStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Subscription is not active for id: " + stripeSubscriptionId);
        }

        subscription.setEndAt(TimeWizard.timeSpellconvert(stripeSub.getCurrentPeriodEnd()));
        subscriptionRepository.save(subscription);

        User user = subscription.getUser();
        if (user == null) {
            throw new IllegalStateException("Subscription " + subscriptionId + " has no linked user");
        }

        Plan plan = subscription.getPlan();
        if (plan == null) {
            throw new IllegalStateException("Subscription " + subscriptionId + " has no linked plan");
        }

        Integer credits = plan.getCredits();
        if (credits == null || credits <= 0) {
            throw new IllegalStateException("Plan for subscription " + subscriptionId + " has invalid credits: " + credits);
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
            throw new IllegalArgumentException("Invoice must not be null");
        }
        if (invoice.getParent() == null || invoice.getParent().getSubscriptionDetails() == null) {
            throw new IllegalArgumentException("Invoice is missing subscription details");
        }

        String stripeSubscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();
        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalArgumentException("Subscription ID is missing in invoice");
        }

        //TODO: find in repository by stripeSubscriptionId, find current active subscription id, set to PAST_DUE
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

        User user = subscription.getUser();
        if (user != null && user.getEmail() != null) {
            System.out.println("Notifying user of failed payment: " + user.getEmail());
        }
    }

    private void handleSubscriptionDeleted(com.stripe.model.Subscription stripeSubscription) {
        if (stripeSubscription == null) {
            throw new IllegalArgumentException("Stripe subscription must not be null");
        }

        String stripeSubscriptionId = stripeSubscription.getId();
        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalArgumentException("Subscription ID not found in Stripe subscription object");
        }

        //TODO: find in repository by stripeSubscriptionId, find current active subscription id
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        subscription.setSubscriptionStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);

        Long cancelAtTimestamp = stripeSubscription.getCancelAt();
        LocalDateTime endAt = (cancelAtTimestamp != null)
                ? LocalDateTime.ofEpochSecond(cancelAtTimestamp, 0, ZoneOffset.UTC)
                : LocalDateTime.now();

        subscription.setEndAt(endAt);
        subscriptionRepository.save(subscription);

        User user = subscription.getUser();
        if (user != null && user.getEmail() != null) {
            System.out.println("Notifying user of subscription cancellation: " + user.getEmail());
        }
    }


    // WORK HERE
    private void handleSubscriptionUpdated(com.stripe.model.Subscription stripeSubscription) {
        if (stripeSubscription == null) {
            throw new IllegalArgumentException("Stripe subscription must not be null");
        }

        String stripeSubscriptionId = stripeSubscription.getId();
        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalStateException("Stripe subscription ID is missing");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id: " + subscriptionId));

        if (stripeSubscription.getItems() == null
                || stripeSubscription.getItems().getData() == null
                || stripeSubscription.getItems().getData().isEmpty()) {
            throw new IllegalStateException("Stripe subscription items are missing for subscription: " + subscriptionId);
        }

        com.stripe.model.SubscriptionItem item = stripeSubscription.getItems().getData().get(0);
        if (item.getPrice() == null || item.getPrice().getId() == null) {
            throw new IllegalStateException("Stripe subscription item has no price ID for subscription: " + subscriptionId);
        }

        String newPlanPriceId = item.getPrice().getId();
        Plan newPlan = planRepository.findByStripePriceId(newPlanPriceId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for Stripe price ID: " + newPlanPriceId));

        subscription.setPlan(newPlan);

        Subscription.SubscriptionStatus status =
                Subscription.SubscriptionStatus.valueOf(stripeSubscription.getStatus().toUpperCase());
        subscription.setSubscriptionStatus(status);

        subscriptionRepository.save(subscription);
    }

    //WORK HERE
    private void handleCheckoutCompleted(Session session) throws StripeException {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null");
        }

        String userId = session.getMetadata().get("userId");
        String planId = session.getMetadata().get("planId");
        String customerId = session.getCustomer();
        String stripeSubscriptionId = session.getSubscription();

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Session is missing userId metadata");
        }
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Session is missing planId metadata");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalStateException("Session is missing customerId");
        }
        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalStateException("Session is missing stripeSubscriptionId");
        }

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        user.setStripeCustomerId(customerId);
        userRepository.save(user);

        Plan plan = planRepository.findById(UUID.fromString(planId))
                .orElseThrow(() -> new EntityNotFoundException("Plan with id " + planId + " not found"));

        Subscription activeSub = subscriptionRepository.findByUserIdAndStatus(user.getUserId(), SubscriptionStatus.ACTIVE);

        // Change current subscription
        if(activeSub != null){
            com.stripe.model.Subscription oldStripeSub =
                    com.stripe.model.Subscription.retrieve(activeSub.getStripeSubscriptionId());
            oldStripeSub.update(
                    com.stripe.param.SubscriptionUpdateParams.builder()
                            .setCancelAt(oldStripeSub.getCurrentPeriodEnd())
                            .build()
            );

            activeSub.setEndAt(TimeWizard.timeSpellconvert(oldStripeSub.getCurrentPeriodEnd()));
            subscriptionRepository.save(activeSub);

            Subscription newSub = new Subscription();
            newSub.setUser(user);
            newSub.setPlan(plan);
            newSub.setStripeSubscriptionId(stripeSubscriptionId); // same Stripe subscription
            newSub.setStartAt(TimeWizard.timeSpellconvert(oldStripeSub.getCurrentPeriodEnd()));
            newSub.setSubscriptionStatus(SubscriptionStatus.PENDING);
            subscriptionRepository.save(newSub);
        }

        // Plan during checkout is a subscription
        if (plan.getPlanType() == Plan.PlanType.MONTH || plan.getPlanType() == Plan.PlanType.YEAR) {
            com.stripe.model.Subscription stripeSubscription = com.stripe.model.Subscription.retrieve(subscriptionId);
            if (stripeSubscription == null) {
                throw new IllegalStateException("Stripe subscription could not be retrieved for id: " + subscriptionId);
            }

            Subscription newSubscription = new Subscription();
            newSubscription.setUser(user);
            newSubscription.setPlan(plan);
            newSubscription.setSubscriptionId(subscriptionId);

            Subscription.SubscriptionStatus status =
                    Subscription.SubscriptionStatus.valueOf(stripeSubscription.getStatus().toUpperCase());
            newSubscription.setSubscriptionStatus(status);
            newSubscription.setStartAt(TimeWizard.timeSpellconvert(stripeSubscription.getStartDate()));
            newSubscription.setEndAt(TimeWizard.timeSpellconvert(stripeSubscription.getCurrentPeriodEnd()));

            subscriptionRepository.save(newSubscription);
        }

        //TODO: work on top-off flow
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

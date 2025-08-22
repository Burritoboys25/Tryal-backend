package com.backend.tryal.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;

import java.util.Map;

public interface PaymentService {
    void handleInvoicePaid(Invoice invoice);
    void handleInvoiceFailed(Invoice invoice);

    void handleSubscriptionDeleted(com.stripe.model.Subscription stripeSubscription);
    void handleSubscriptionUpdated(com.stripe.model.Subscription stripeSubscription);
    void handleCheckoutCompleted(Session session) throws StripeException;

    String createCheckoutSession(String userId, String userEmail, String planId);
    Map<String, String> getSessionStatus(String sessionId);

    void processStripeEvent(String payload, String sigHeader) throws StripeException
}

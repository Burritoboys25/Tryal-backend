package com.backend.tryal.stripe.service;

import com.stripe.exception.StripeException;
import java.util.Map;

public interface StripeService {

    String createCheckoutSession(String userId, String userEmail, String planId);
    Map<String, String> getSessionStatus(String sessionId);

    void processStripeEvent(String payload, String sigHeader) throws StripeException;
}

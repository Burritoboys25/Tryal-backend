package com.backend.tryal.stripe.controller;

import java.util.Map;

import com.backend.tryal.stripe.service.StripeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/session")
public class StripeController {
    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, String> requestBody) {
        String userEmail = requestBody.get("email");
        String userId = requestBody.get("userId");
        String planId = requestBody.get("planId");

        if (userEmail == null || userEmail.isBlank()
                || userId == null || userId.isBlank()
                || planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Missing required fields: email, userId, or planId");
        }

        String clientSecret = stripeService.createCheckoutSession(userId, userEmail, planId);
        return Map.of("clientSecret", clientSecret);
    }

    @GetMapping
    public Map<String, String> getSessionStatus(@RequestParam("session_id") String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Missing session ID");
        }

        return stripeService.getSessionStatus(sessionId);
    }
}

package com.backend.tryal.stripe.controller;

import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.stripe.service.StripeService;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/webhooks/stripe")
public class StripeWebhookController {

    private final StripeService stripeService;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    // IMPORTANT: Stripe signs the exact raw request body to generate the webhook signature.
    // Using @RequestBody or reading the body as parsed JSON would alter the payload
    // (e.g., formatting, whitespace), which would cause signature verification to fail.
    @PostMapping
    public ApiResponse<String> handleWebhook(HttpServletRequest request, @RequestHeader("Stripe-Signature") String sigHeader) throws IOException, StripeException {
        String payload = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        stripeService.processStripeEvent(payload, sigHeader);

        return new ApiResponse<>("Webhook processed successfully");
    }
}

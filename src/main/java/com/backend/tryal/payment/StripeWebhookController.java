package com.backend.tryal.payment;

import com.backend.tryal.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/webhooks/stripe")
public class StripeWebhookController {

    private final PaymentService paymentService;

    public StripeWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // IMPORTANT: Stripe signs the exact raw request body to generate the webhook signature.
    // Using @RequestBody or reading the body as parsed JSON would alter the payload
    // (e.g., formatting, whitespace), which would cause signature verification to fail.
    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request, @RequestHeader("Stripe-Signature") String sigHeader){
        String payload;

        try {
            payload = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to read payload");
        }

        try {
            paymentService.processStripeEvent(payload, sigHeader);
            return new ResponseEntity<>("Webhook processed successfully", HttpStatus.OK);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("Invalid Signature", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong while processing webhook", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

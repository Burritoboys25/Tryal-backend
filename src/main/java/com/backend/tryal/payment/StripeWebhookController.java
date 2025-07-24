package com.backend.tryal.payment;

import com.backend.tryal.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.model.checkout.Session;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/webhooks/stripe")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final PaymentService paymentService;

    public StripeWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // IMPORTANT: Stripe signs the exact raw request body to generate the webhook signature.
    // Using @RequestBody or reading the body as parsed JSON would alter the payload
    // (e.g., formatting, whitespace), which would cause signature verification to fail.
    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        String payload;

        try {
            ServletInputStream inputStream = request.getInputStream();
            byte[] bytes = inputStream.readAllBytes();
            payload = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to read payload");
            return ResponseEntity.badRequest().body("Failed to read payload");
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("Invalid signature");
            return ResponseEntity.status(400).body("Invalid Signature");
        }

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = deserializer.getObject().orElse(null);

        if (stripeObject == null) {
            System.out.println("Deserialization failed");
            return ResponseEntity.status(400).body("Deserialization failed");
        }

        logReceivedEvent(event);

        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) stripeObject;
                paymentService.handleCheckoutCompleted(session);
                System.out.println("Checkout completed: " + session);
                return ResponseEntity.ok("Subscription saved");

            case "invoice.payment_succeeded":
                Invoice invoice = (Invoice) stripeObject;
                paymentService.handleInvoicePaid(invoice);
                return ResponseEntity.ok("Invoice paid");

            case "invoice.payment_failed":
                Invoice failedInvoice = (Invoice) stripeObject;
                System.out.println("Invoice payment failed: " + failedInvoice.getId());
                // TODO: Update user status as at-risk or inactive
                // TODO: Stop token crediting or access
                break;

            case "customer.subscription.updated":
                // Requires fetching full Subscription object (not included in event data directly)
                System.out.println("Subscription updated");
                // TODO: Update user's subscription tier/plan in the database
                break;

            case "customer.subscription.deleted":
                System.out.println("Subscription canceled or deleted");
                // TODO: Mark user as unsubscribed
                // TODO: Stop access and token crediting
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

        return ResponseEntity.ok("Webhook received");
    }

    // TODO: If we want to save and log data into the db
    private void logReceivedEvent(Event event) {
        System.out.printf("Received Stripe event: id=%s, type=%s, created=%d%n",
                event.getId(),
                event.getType(),
                event.getCreated());
    }
}

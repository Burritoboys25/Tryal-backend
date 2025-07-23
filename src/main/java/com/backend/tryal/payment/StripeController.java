package com.backend.tryal.payment;

import java.util.Map;
import java.util.HashMap;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/session")
public class StripeController {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final String domain = "http://localhost:3000";

    @PostMapping()
    public ResponseEntity<?> createCheckoutSession(@RequestBody Map<String, String> requestBody) {
        Stripe.apiKey = stripeSecretKey;

        String priceId = requestBody.get("priceId");
        String userEmail = requestBody.get("email");
        String userId = requestBody.get("userId");
        String planId = requestBody.get("planId");


        if(priceId == null || userEmail == null || userId == null ){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            SessionCreateParams params = SessionCreateParams.builder()
                    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setReturnUrl(domain + "/stripe/return?session_id={CHECKOUT_SESSION_ID}")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice(priceId)
                                    .build()
                    )
                    .setSubscriptionData(
                            SessionCreateParams.SubscriptionData.builder()
                                    .putMetadata("userId", userId)
                                    .putMetadata("planId", planId)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", session.getClientSecret());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, String>> getSessionStatus(@RequestParam("session_id") String sessionId) {
        Stripe.apiKey = stripeSecretKey;

        try {
            Session session = Session.retrieve(sessionId);

            Map<String, String> response = new HashMap<>();
            response.put("status", session.getStatus());

            if (session.getCustomerDetails() != null && session.getCustomerDetails().getEmail() != null) {
                response.put("customer_email", session.getCustomerDetails().getEmail());
            } else {
                response.put("customer_email", "unknown");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

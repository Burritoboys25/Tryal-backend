package com.backend.tryal.payment;

import java.util.Map;
import com.backend.tryal.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/session")
public class StripeController {
    private final PaymentService paymentService;

    public StripeController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createCheckoutSession(@RequestBody Map<String, String> requestBody) {
        String userEmail = requestBody.get("email");
        String userId = requestBody.get("userId");
        String planId = requestBody.get("planId");

        if (planId == null || userEmail == null || userId == null) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }

        try {
            String clientSecret = paymentService.createCheckoutSession(userId, userEmail, planId);
            return new ResponseEntity<>(Map.of("clientSecret", clientSecret), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getSessionStatus(@RequestParam("session_id") String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return new ResponseEntity<>("Missing session ID", HttpStatus.BAD_REQUEST);
        }

        try {
            Map<String, String> response = paymentService.getSessionStatus(sessionId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

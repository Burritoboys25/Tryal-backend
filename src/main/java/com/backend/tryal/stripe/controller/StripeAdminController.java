package com.backend.tryal.stripe.controller;

import com.backend.tryal.stripe.service.StripeAdminService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stripe")
public class StripeAdminController {

    private final StripeAdminService stripeAdminService;

    public StripeAdminController(StripeAdminService stripeAdminService) {
        this.stripeAdminService = stripeAdminService;
    }

    @PostMapping("/sync-plans")
    public ResponseEntity<String> syncPlans() throws StripeException {
        stripeAdminService.syncPlansFromStripe();
        return new ResponseEntity<>("Stripe plans synced successfully.", HttpStatus.OK);
    }
}

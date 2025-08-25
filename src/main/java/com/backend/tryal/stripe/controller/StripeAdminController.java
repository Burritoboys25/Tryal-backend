package com.backend.tryal.stripe.controller;

import com.backend.tryal.stripe.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stripe")
public class StripeAdminController {

    private final StripeService stripeService;

    public StripeAdminController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("sync-plans")
    public ResponseEntity<String> syncPlans(){
        try {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

package com.backend.tryal.stripe.controller;

import com.backend.tryal.stripe.dto.StripePriceRequestDTO;
import com.backend.tryal.stripe.dto.StripeProductRequestDTO;
import com.backend.tryal.stripe.service.StripeAdminService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/stripe")
public class StripeAdminController {

    private final StripeAdminService stripeAdminService;

    public StripeAdminController(StripeAdminService stripeAdminService) {
        this.stripeAdminService = stripeAdminService;
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncPlans() throws StripeException {
        stripeAdminService.syncPlansFromStripe();
        return new ResponseEntity<>("Stripe plans synced successfully.", HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody StripeProductRequestDTO stripeProductRequestDTO) throws StripeException {
        stripeAdminService.createProduct(stripeProductRequestDTO);
        return new ResponseEntity<>("Stripe product created successfully.", HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/prices")
    public ResponseEntity<String> createPrice(@PathVariable String productId, @RequestBody StripePriceRequestDTO stripePriceRequestDTO) throws StripeException {
        stripeAdminService.createPrice(productId, stripePriceRequestDTO);
        return new ResponseEntity<>("Stripe price created successfully.", HttpStatus.OK);
    }

    @PostMapping("/prices/{priceId}/deactivate")
    public ResponseEntity<String> deactivatePrice(@PathVariable String priceId) throws StripeException {
        stripeAdminService.deactivatePrice(priceId);
        return new ResponseEntity<>("Stripe price deactivated successfully.", HttpStatus.OK);
    }
}

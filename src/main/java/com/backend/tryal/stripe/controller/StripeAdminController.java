package com.backend.tryal.stripe.controller;

import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.stripe.dto.StripePriceRequestDTO;
import com.backend.tryal.stripe.dto.StripeProductRequestDTO;
import com.backend.tryal.stripe.service.StripeAdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stripe")
public class StripeAdminController {

    private final StripeAdminService stripeAdminService;

    public StripeAdminController(StripeAdminService stripeAdminService) {
        this.stripeAdminService = stripeAdminService;
    }

    @PostMapping("/sync")
    public ApiResponse<String> syncPlans() throws StripeException {
        stripeAdminService.syncPlansFromStripe();
        return new ApiResponse<>("Stripe plans synced successfully.");
    }

    @PostMapping("/products")
    public ApiResponse<Map<String, Object>> createProduct(@RequestBody StripeProductRequestDTO stripeProductRequestDTO) throws StripeException, JsonProcessingException {
        return new ApiResponse<>(stripeAdminService.createProduct(stripeProductRequestDTO));
    }

    @PostMapping("/products/{productId}/prices")
    public ApiResponse<Map<String, Object>> createPrice(@PathVariable String productId, @RequestBody StripePriceRequestDTO stripePriceRequestDTO) throws StripeException, JsonProcessingException {
        return new ApiResponse<>(stripeAdminService.createPrice(productId, stripePriceRequestDTO));
    }

    @PostMapping("/prices/{priceId}/deactivate")
    public ApiResponse<Map<String, Object>> deactivatePrice(@PathVariable String priceId) throws StripeException, JsonProcessingException {
        return new ApiResponse<>(stripeAdminService.deactivatePrice(priceId));
    }
}

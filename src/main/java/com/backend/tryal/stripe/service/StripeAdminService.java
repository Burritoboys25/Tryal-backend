package com.backend.tryal.stripe.service;

import com.backend.tryal.stripe.dto.StripePriceRequestDTO;
import com.backend.tryal.stripe.dto.StripeProductRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;

import java.util.Map;

public interface StripeAdminService {
    void syncPlansFromStripe() throws StripeException;

    Map<String, Object> createProduct(StripeProductRequestDTO stripeProductRequestDTO) throws StripeException, JsonProcessingException;

    Map<String, Object> createPrice(String productId, StripePriceRequestDTO stripePriceRequestDTO) throws StripeException, JsonProcessingException;

    Map<String, Object> deactivatePrice(String priceId) throws StripeException, JsonProcessingException;
}

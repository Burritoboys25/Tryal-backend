package com.backend.tryal.stripe.service;

import com.backend.tryal.stripe.dto.StripePriceRequestDTO;
import com.backend.tryal.stripe.dto.StripeProductRequestDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;

public interface StripeAdminService {
    void syncPlansFromStripe() throws StripeException;

    Product createProduct(StripeProductRequestDTO stripeProductRequestDTO) throws StripeException;

    Price createPrice(String productId, StripePriceRequestDTO stripePriceRequestDTO) throws StripeException;

    Price deactivatePrice(String priceId) throws StripeException;
}

package com.backend.tryal.stripe.service;

import com.stripe.exception.StripeException;

public interface StripeAdminService {
    void syncPlansFromStripe() throws StripeException;
}

package com.backend.tryal.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;

public interface PaymentService {
    void handleInvoicePaid(Invoice invoice);
    void handleCheckoutCompleted(Session session) throws StripeException;
}

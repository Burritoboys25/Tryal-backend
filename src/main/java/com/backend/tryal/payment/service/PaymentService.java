package com.backend.tryal.payment.service;

import com.stripe.model.*;

public interface PaymentService {
    boolean handleInvoicePaid(Invoice invoice);
}

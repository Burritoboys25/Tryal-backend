package com.backend.tryal.stripe.dto;

import com.backend.tryal.plan.Plan;
import lombok.Data;

@Data
public class StripePriceRequestDTO {
    //Currency must be in lowercase
    public enum Currency {
        usd,
        vnd
    }

    public enum TaxBehavior {
        EXCLUSIVE,
        INCLUSIVE,
        UNSPECIFIED
    }

    private Currency currency;
    private Boolean isActive;
    private Integer credits;
    private Boolean rolloverCreditsAllowed;
    private Plan.PlanType planType;

    //Price is in cents
    private Long price;
    private TaxBehavior taxBehavior;
}

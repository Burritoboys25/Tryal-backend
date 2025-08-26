package com.backend.tryal.stripe.dto;

import lombok.Data;

@Data
public class StripeProductRequestDTO {
    private String name;
    private Boolean isActive;
    private String description;
}

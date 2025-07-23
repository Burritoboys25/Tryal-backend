package com.backend.tryal.business.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BusinessDTO {
    private UUID businessId;
    private String stripeAccountId;
    private String name;
    private String email;
    private String website;
    private String address;
    private String phoneNumber;

    private int minCredits;
    private int maxCredits;
}

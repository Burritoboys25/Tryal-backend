package com.backend.tryal.business.dto;

import com.backend.tryal.business.Business;
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
    private Business.OnboardingStatus onboardingStatus;

    private int minCredits;
    private int maxCredits;
}

package com.backend.tryal.business.dto;

import com.backend.tryal.business.Business;
import lombok.Data;

@Data
public class OnboardingStatusUpdateRequest {
    private Business.OnboardingStatus onboardingStatus;

    public OnboardingStatusUpdateRequest(Business.OnboardingStatus onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }
}

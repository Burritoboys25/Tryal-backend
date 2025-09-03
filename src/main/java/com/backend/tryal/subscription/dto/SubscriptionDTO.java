package com.backend.tryal.subscription.dto;

import com.backend.tryal.subscription.Subscription;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class SubscriptionDTO {
    private String subscriptionId;
    private UUID userId;
    private UUID planId;
    private Subscription.SubscriptionStatus subscriptionStatus;
    private Boolean autoRenew;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public SubscriptionDTO(){}

    public SubscriptionDTO(String subscriptionId, UUID userId, UUID planId, Subscription.SubscriptionStatus subscriptionStatus, Boolean autoRenew, LocalDateTime startAt, LocalDateTime endAt) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.planId = planId;
        this.subscriptionStatus = subscriptionStatus;
        this.autoRenew = autoRenew;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}

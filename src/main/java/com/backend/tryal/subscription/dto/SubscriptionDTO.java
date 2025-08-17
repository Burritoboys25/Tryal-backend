package com.backend.tryal.subscription.dto;

import com.backend.tryal.subscription.Subscription;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public Subscription.SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(Subscription.SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

}

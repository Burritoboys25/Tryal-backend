package com.backend.tryal.subscription.dto;

import com.backend.tryal.subscription.Subscription;

import java.time.LocalDateTime;
import java.util.UUID;

public class SubscriptionDTO {

    private UUID subscriptionId;
    private UUID userId;
    private UUID planId;
    private Subscription.SubscriptionStatus subscriptionStatus;
    private Boolean autoRenew;
    private String stripeSubscriptionId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public SubscriptionDTO(){}

    public SubscriptionDTO(UUID subscriptionId, UUID userId, UUID planId, Subscription.SubscriptionStatus subscriptionStatus, Boolean autoRenew, String stripeSubscriptionId, LocalDateTime startAt, LocalDateTime endAt) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.planId = planId;
        this.subscriptionStatus = subscriptionStatus;
        this.autoRenew = autoRenew;
        this.startAt = startAt;
        this.endAt = endAt;
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(UUID subscriptionId) {
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

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }
}

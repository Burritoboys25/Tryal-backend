package com.backend.tryal.subscription;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.plan.Plan;
import com.backend.tryal.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

public class Subscription {
    public enum SubscriptionStatus {
        PENDING,
        ACTIVE,
        CANCELLED,
        PAUSED
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "subscription_id")
    private UUID subscriptionId;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = true)
    private SubscriptionStatus subscriptionStatus;

    @Column(name = "auto_renew", nullable = true)
    private Boolean autoRenew;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    @Column(name = "start_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime startAt;

    @Column(name = "end_at",  nullable = true)
    private LocalDateTime endAt;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Subscription(){
    }

    public Subscription(UUID subscriptionId, User user, Plan plan, SubscriptionStatus subscriptionStatus, Boolean autoRenew, String stripeSubscriptionId, LocalDateTime startAt, LocalDateTime endAt) {
        this.subscriptionId = subscriptionId;
        this.user = user;
        this.plan = plan;
        this.subscriptionStatus = subscriptionStatus;
        this.autoRenew = autoRenew;
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
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

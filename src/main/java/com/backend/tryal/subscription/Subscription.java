package com.backend.tryal.subscription;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    public enum SubscriptionStatus {
        PENDING,
        INCOMPLETE,
        INCOMPLETE_EXPIRED,
        PAST_DUE,
        UNPAID,
        ACTIVE,
        CANCELLED,
        PAUSED
    }

    @Id
    @Column(name = "subscription_id", nullable = false, updatable = false)
    private String subscriptionId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
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

    public Subscription(String subscriptionId, User user, Plan plan, SubscriptionStatus subscriptionStatus, Boolean autoRenew, LocalDateTime startAt, LocalDateTime endAt) {
        this.subscriptionId = subscriptionId;
        this.user = user;
        this.plan = plan;
        this.subscriptionStatus = subscriptionStatus;
        this.autoRenew = autoRenew;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}

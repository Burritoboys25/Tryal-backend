package com.backend.tryal.plan;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "plans")
public class Plan {
    public enum PlanType {
        MONTH,
        YEAR,
        ONE_TIME
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "rollover_credits_allowed")
    private Boolean rolloverCreditsAllowed;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "stripe_product_id")
    private String stripeProductId;

    @Column(name = "stripe_price_id", unique = true, nullable = false)
    private String stripePriceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Plan() {
    }

    public Plan(String name, String description, Double price, Integer credits, Boolean rolloverCreditsAllowed, Boolean isActive, String stripeProductId, String stripePriceId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.credits = credits;
        this.rolloverCreditsAllowed = rolloverCreditsAllowed;
        this.isActive = isActive;
        this.stripeProductId = stripeProductId;
        this.stripePriceId = stripePriceId;
    }
}

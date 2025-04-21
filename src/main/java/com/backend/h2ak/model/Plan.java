package com.backend.h2ak.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "plans")
public class Plan {
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

    @Column(name = "monthly_credits")
    private Integer monthlyCredits;

    @Column(name = "rollover_credits_allowed")
    private Boolean rolloverCreditsAllowed;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "stripe_product_id")
    private String stripeProductId;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    public Plan(String name, String description, Double price, Integer monthlyCredits, Boolean rolloverCreditsAllowed, Boolean isActive, String stripeProductId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.monthlyCredits = monthlyCredits;
        this.rolloverCreditsAllowed = rolloverCreditsAllowed;
        this.isActive = isActive;
        this.stripeProductId = stripeProductId;
    }
}

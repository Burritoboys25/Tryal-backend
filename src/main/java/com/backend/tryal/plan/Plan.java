package com.backend.tryal.plan;

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

    @Column(name = "stripe_price_id")
    private String stripePriceId;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Plan() {
    }

    public Plan(String name, String description, Double price, Integer monthlyCredits, Boolean rolloverCreditsAllowed, Boolean isActive, String stripeProductId, String stripePriceId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.monthlyCredits = monthlyCredits;
        this.rolloverCreditsAllowed = rolloverCreditsAllowed;
        this.isActive = isActive;
        this.stripeProductId = stripeProductId;
        this.stripePriceId = stripePriceId;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getMonthlyCredits() {
        return monthlyCredits;
    }

    public void setMonthlyCredits(Integer monthlyCredits) {
        this.monthlyCredits = monthlyCredits;
    }

    public Boolean getRolloverCreditsAllowed() {
        return rolloverCreditsAllowed;
    }

    public void setRolloverCreditsAllowed(Boolean rolloverCreditsAllowed) {
        this.rolloverCreditsAllowed = rolloverCreditsAllowed;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getStripeProductId() {
        return stripeProductId;
    }

    public void setStripeProductId(String stripeProductId) {
        this.stripeProductId = stripeProductId;
    }

    public String getStripePriceId() {
        return stripePriceId;
    }

    public void setStripePriceId(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }
}

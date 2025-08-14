package com.backend.tryal.plan.dto;

import com.backend.tryal.plan.Plan;
import lombok.Data;

import java.util.UUID;

@Data
public class PlanDTO {
    private UUID planId;
    private String name;
    private String description;
    private Double price;
    private Integer credits;
    private Boolean rolloverCreditsAllowed;
    private Plan.PlanType planType;

    public PlanDTO() {}

    public PlanDTO(UUID planId, String name, String description, Double price, Integer credits, Boolean rolloverCreditsAllowed, Plan.PlanType planType) {
        this.planId = planId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.credits = credits;
        this.rolloverCreditsAllowed = rolloverCreditsAllowed;
        this.planType = planType;
    }
}

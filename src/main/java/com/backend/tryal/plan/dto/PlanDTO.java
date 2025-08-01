package com.backend.tryal.plan.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PlanDTO {
    private UUID planId;
    private String name;
    private String description;
    private Double price;
    private Integer monthlyCredits;
    private Boolean rolloverCreditsAllowed;

    public PlanDTO() {}

    public PlanDTO(UUID planId, String name, String description, Double price, Integer monthlyCredits, Boolean rolloverCreditsAllowed) {
        this.planId = planId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.monthlyCredits = monthlyCredits;
        this.rolloverCreditsAllowed = rolloverCreditsAllowed;
    }
}

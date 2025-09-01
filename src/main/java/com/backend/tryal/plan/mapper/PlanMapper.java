package com.backend.tryal.plan.mapper;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.dto.PlanDTO;

public class PlanMapper {

    public static PlanDTO mapPlanDTO(Plan plan) {
        return new PlanDTO(
                plan.getPlanId(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice(),
                plan.getCredits(),
                plan.getRolloverCreditsAllowed(),
                plan.getPlanType(),
                plan.getIsActive()
        );
    }
}

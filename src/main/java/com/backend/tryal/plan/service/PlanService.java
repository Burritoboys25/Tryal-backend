package com.backend.tryal.plan.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.dto.PlanDTO;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    List<PlanDTO> getAllActivePlans();
    Plan getPlanById(UUID planId);
    Plan createPlan(Plan plan);
    void deactivatePlanById(UUID planId);
    void reactivatePlanById(UUID planId);
}

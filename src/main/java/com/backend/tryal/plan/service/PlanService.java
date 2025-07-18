package com.backend.tryal.plan.service;

import com.backend.tryal.plan.Plan;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    List<Plan> getAllActivePlans();
    Plan getPlanById(UUID planId);
    Plan createPlan(Plan plan);
    boolean deactivatePlanById(UUID planId);
    boolean reactivatePlanById(UUID planId);
}

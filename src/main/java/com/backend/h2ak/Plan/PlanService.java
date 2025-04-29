package com.backend.h2ak.Plan;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    List<Plan> getAllPlans();
    Plan getPlanById(UUID planId);
    Plan createPlan(Plan plan);
    Plan updatePlanById(UUID planId, Plan plan);
    boolean deletePlanById(UUID plan);
}

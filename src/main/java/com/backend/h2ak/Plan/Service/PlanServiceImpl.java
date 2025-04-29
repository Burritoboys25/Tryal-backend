package com.backend.h2ak.Plan.Service;

import com.backend.h2ak.Plan.Plan;
import com.backend.h2ak.Plan.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public Plan getPlanById(UUID planId) {
        return planRepository.findById(planId).orElse(null);
    }

    @Override
    public Plan createPlan(Plan plan) {
        planRepository.save(plan);

        return plan;
    }

    @Override
    public Plan updatePlanById(UUID planId, Plan plan) {
        if (getPlanById(planId) != null) {
            Plan updatedPlan = getPlanById(planId);

            if (plan.getName() != null) {
                updatedPlan.setName(plan.getName());
            }

            if (plan.getDescription() != null) {
                updatedPlan.setDescription(plan.getDescription());
            }

            if (plan.getPrice() != null) {
                updatedPlan.setPrice(plan.getPrice());
            }

            if (plan.getMonthlyCredits() != null) {
                updatedPlan.setMonthlyCredits(plan.getMonthlyCredits());
            }

            if (plan.getRolloverCreditsAllowed() != null) {
                updatedPlan.setRolloverCreditsAllowed(plan.getRolloverCreditsAllowed());
            }

            if (plan.getActive() != null) {
                updatedPlan.setActive(plan.getActive());
            }

            if (plan.getStripeProductId() != null) {
                updatedPlan.setStripeProductId(plan.getStripeProductId());
            }

            planRepository.save(updatedPlan);
            return updatedPlan;
        }
        return null;
    }

    @Override
    public boolean deletePlanById(UUID planId) {
        if (getPlanById(planId) != null) {
            planRepository.deleteById(planId);
            return true;
        }

        return false;
    }
}

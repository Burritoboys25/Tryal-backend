package com.backend.tryal.plan.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> getAllActivePlans() {
        return planRepository.findAll().stream().filter(plan -> plan.getActive() == true).collect(Collectors.toList()));
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
    public boolean deactivatePlanById(UUID planId) {
        Plan plan = getPlanById(planId);

        if(plan == null){
            return false;
        }

        plan.setActive(false);
        planRepository.save(plan);

        return true;
    }

    @Override
    public boolean reactivatePlanById(UUID planId) {
        Plan plan = getPlanById(planId);

        if(plan == null){
            return false;
        }

        plan.setActive(true);
        planRepository.save(plan);

        return true;
    }
}

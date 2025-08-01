package com.backend.tryal.plan.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.plan.dto.PlanDTO;
import com.backend.tryal.plan.mapper.PlanMapper;
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
    public List<PlanDTO> getAllActivePlans() {
        return planRepository.findAll().stream()
                .filter(Plan::getIsActive)
                .map(PlanMapper::mapPlanDTO)
                .collect(Collectors.toList());
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

        plan.setIsActive(false);
        planRepository.save(plan);

        return true;
    }

    @Override
    public boolean reactivatePlanById(UUID planId) {
        Plan plan = getPlanById(planId);

        if(plan == null){
            return false;
        }

        plan.setIsActive(true);
        planRepository.save(plan);

        return true;
    }
}

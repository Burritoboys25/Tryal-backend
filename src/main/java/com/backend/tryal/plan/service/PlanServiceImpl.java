package com.backend.tryal.plan.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.plan.dto.PlanDTO;
import com.backend.tryal.plan.mapper.PlanMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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
    Plan plan = planRepository.findById(planId).orElse(null);
    if (plan == null) {
      throw new EntityNotFoundException("Plan not found with id: " + planId);
    }
    return plan;
  }

  @Override
  public Plan createPlan(Plan plan) {
    planRepository.save(plan);
    return plan;
  }

  @Override
  public void deactivatePlanById(UUID planId) {
    Plan plan = planRepository.findById(planId).orElse(null);
    if (plan == null) {
      throw new EntityNotFoundException("Plan not found with id: " + planId);
    }

    plan.setIsActive(false);
    planRepository.save(plan);
  }

  @Override
  public void reactivatePlanById(UUID planId) {
    Plan plan = planRepository.findById(planId).orElse(null);
    if (plan == null) {
      throw new EntityNotFoundException("Plan not found with id: " + planId);
    }

    plan.setIsActive(true);
    planRepository.save(plan);
  }
}

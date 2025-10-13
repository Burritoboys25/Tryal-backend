package com.backend.tryal.plan;

import com.backend.tryal.plan.dto.PlanDTO;
import com.backend.tryal.plan.service.PlanService;
import com.backend.tryal.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

  private final PlanService planService;

  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  // get all Plans
  @GetMapping()
  public List<PlanDTO> getAllActivePlans() {
    return planService.getAllActivePlans();
  }

  // get Plan by ID
  @GetMapping("/{planId}")
  public Plan getPlanById(@PathVariable UUID planId) {
    return planService.getPlanById(planId);
  }

  // Create Plan
  @PostMapping()
  public Plan createPlan(@RequestBody Plan plan) {
    return planService.createPlan(plan);
  }

  // Deactivate Plan
//  @PatchMapping("/{planId}/deactivate")
//  public ApiResponse<String> deactivatePlanById(@PathVariable UUID planId) {
//    planService.deactivatePlanById(planId);
//    return new ApiResponse<>("Plan deactivated successfully.");
//  }

  // Reactivate Plan
//  @PatchMapping("/{planId}/reactivate")
//  public ApiResponse<String> reactivatePlanById(@PathVariable UUID planId) {
//    planService.reactivatePlanById(planId);
//    return new ApiResponse<>("Plan reactivated successfully.");
//  }
}

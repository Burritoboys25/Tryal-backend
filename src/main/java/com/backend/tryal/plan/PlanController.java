package com.backend.tryal.plan;

import com.backend.tryal.plan.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // get all Plans
    @GetMapping()
    public ResponseEntity<List<Plan>> getAllPlans() {
        try {
            List<Plan> plans = new ArrayList<Plan>(planService.getAllPlans());

            if (plans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(plans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get Plan by ID
    @GetMapping("/{planId}")
    public ResponseEntity<Plan> getPlanById(@PathVariable UUID planId) {
        try {
            Plan plan = planService.getPlanById(planId);

            if (plan == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(plan, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create Plan
    @PostMapping()
    public ResponseEntity<Plan> createUser(@RequestBody Plan plan) {
        try{
            Plan newPlan = planService.createPlan(plan);
            return new ResponseEntity<>(newPlan, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Patch Plan
    @PatchMapping("/{planId}")
    public ResponseEntity<Plan> updatePlanById(@RequestBody Plan plan, @PathVariable UUID planId) {
        try {
            Plan updatedPlan = planService.updatePlanById(planId, plan);

            if (updatedPlan == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(updatedPlan, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Plan
    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlanById(@PathVariable UUID planId) {
        try {
            if (planService.deletePlanById(planId)) {
                return new ResponseEntity<>("Plan deleted successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Plan not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

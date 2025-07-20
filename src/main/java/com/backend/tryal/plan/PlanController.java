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
    public ResponseEntity<List<Plan>> getAllActivePlans() {
        try {
            List<Plan> plans = new ArrayList<Plan>(planService.getAllActivePlans());

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
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        try{
            Plan newPlan = planService.createPlan(plan);
            return new ResponseEntity<>(newPlan, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Deactivate Plan
    @PatchMapping("/{planId}/deactivate")
    public ResponseEntity<String> deactivatePlanById(@PathVariable UUID planId) {
        try {
            if (planService.deactivatePlanById(planId)) {
                return new ResponseEntity<>("Plan deactivated successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Plan not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Reactivate Plan
    @PatchMapping("/{planId}/reactivate")
    public ResponseEntity<String> reactivatePlanById(@PathVariable UUID planId) {
        try {
            if (planService.reactivatePlanById(planId)) {
                return new ResponseEntity<>("Plan reactivated successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Plan not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

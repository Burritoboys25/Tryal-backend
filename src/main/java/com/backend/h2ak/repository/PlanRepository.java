package com.backend.h2ak.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import com.backend.h2ak.model.Plan;

//extends JpaRepository<Plan, UUID>

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    void deleteByPlanId(UUID id);

//    Optional<Plan> findByPlanId(UUID id);
}

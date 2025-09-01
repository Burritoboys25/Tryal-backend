package com.backend.tryal.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByStripePriceId(String stripePriceId);

    @Modifying
    @Query("UPDATE Plan p SET p.isActive = false WHERE p.stripePriceId NOT IN :activeIds")
    void markInactivePlansNotIn(@Param("activeIds") Set<String> activeIds);
}

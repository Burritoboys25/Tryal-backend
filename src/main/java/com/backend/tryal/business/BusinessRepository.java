package com.backend.tryal.business;

import com.backend.tryal.experience.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    Optional<Business> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("SELECT DISTINCT b FROM Business b " +
            "JOIN b.experiences e " +
            "JOIN e.categories c " +
            "WHERE (:categoryIds IS NULL OR c.id IN :categoryIds) " +
            "AND (:minDuration IS NULL OR e.duration >= :minDuration) " +
            "AND (:skillLevel IS NULL OR e.skillLevel = :skillLevel)")
    List<Business> findFilteredBusinesses(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("minDuration") Integer minDuration,
            @Param("skillLevel") Experience.SkillLevel skillLevel
    );
}

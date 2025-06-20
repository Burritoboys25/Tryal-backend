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
            "WHERE (:categoryIds IS NULL OR c.categoryId IN :categoryIds) " +
            "AND (:duration IS NULL OR e.duration <= :duration) " +
            "AND (:skillLevel IS NULL OR e.skillLevel IN :skillLevel)")
    List<Business> findFilteredBusinesses(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("duration") Integer duration,
            @Param("skillLevel") List<Experience.SkillLevel> skillLevel
    );
}

package com.backend.tryal.business;

import com.backend.tryal.business.dto.BusinessCreditRangeDTO;
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
            "JOIN e.categories ec " +
            "JOIN e.groupTypes gt " +
            "WHERE (:categoryIds IS NULL OR ec.categoryId IN :categoryIds) " +
            "AND (:groupTypeIds IS NULL OR gt.groupTypeId = :groupTypeIds) " +
            "AND (:skillLevels IS NULL OR e.skillLevel IN :skillLevels) " +
            "AND (:duration IS NULL OR e.duration <= :duration) " +
            "AND (:creditsMin IS NULL OR e.creditPrice >= :creditsMin) " +
            "AND (:creditsMax IS NULL OR e.creditPrice <= :creditsMax)")
    List<Business> findFilteredBusinesses(
            @Param("categoryIds") List<UUID> categoryIds,
            @Param("groupTypeIds") UUID groupTypeIds,
            @Param("skillLevels") List<Experience.SkillLevel> skillLevels,
            @Param("duration") Integer duration,
            @Param("creditsMin") Integer creditsMin,
            @Param("creditsMax") Integer creditsMax
    );

    @Query(
            value = """
        SELECT
            b.business_id as businessId,
            b.name as businessName,
            MIN(e.credit_price) AS minCredit,
            MAX(e.credit_price) AS maxCredit
        FROM businesses b
        JOIN experiences e
            ON b.business_id = e.business_id
        WHERE b.business_id = :businessId
        GROUP BY b.business_id, b.name
        """,
            nativeQuery = true
    )
    BusinessCreditRangeDTO getBusinessCreditRangeById(@Param("businessId") UUID businessId);
}

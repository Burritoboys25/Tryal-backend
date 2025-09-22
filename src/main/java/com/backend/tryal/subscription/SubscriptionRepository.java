package com.backend.tryal.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId")
    List<Subscription> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND s.subscriptionStatus = 'ACTIVE'")
    List<Subscription> findByUserIdAndActive(@Param("userId") UUID userId);
}

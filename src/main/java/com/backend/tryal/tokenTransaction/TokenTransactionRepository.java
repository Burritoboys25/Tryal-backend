package com.backend.tryal.tokenTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TokenTransactionRepository extends JpaRepository<TokenTransaction, UUID>{
    @Query("SELECT t FROM TokenTransaction t WHERE t.user.userId = :userId")
    List<TokenTransaction> findByUserId(@Param("userId") UUID userId);
}

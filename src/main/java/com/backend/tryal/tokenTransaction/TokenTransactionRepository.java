package com.backend.tryal.tokenTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TokenTransactionRepository extends JpaRepository<TokenTransaction, UUID>{
    List<TokenTransaction> findByUserId(UUID userId);
}

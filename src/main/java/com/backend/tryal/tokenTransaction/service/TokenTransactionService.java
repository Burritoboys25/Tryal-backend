package com.backend.tryal.tokenTransaction.service;

import com.backend.tryal.tokenTransaction.TokenTransaction;
import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;

import java.util.List;
import java.util.UUID;

public interface TokenTransactionService {
    List<TokenTransaction> getAllTokenTransactions();
    List<TokenTransaction> getAllTokenTransactions(UUID userId);

    TokenTransaction getTokenTransactionById(UUID transactionId);
    TokenTransaction createTokenTransaction(UUID userId, TokenTransactionDTO tokenTransactionDTO);
}

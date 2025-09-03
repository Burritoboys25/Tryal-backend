package com.backend.tryal.tokenTransaction.dto;

import com.backend.tryal.tokenTransaction.TokenTransaction;

import java.util.UUID;
import lombok.Data;

@Data
public class TokenTransactionDTO {
    private UUID tokenTransId;
    private UUID userId;
    private Long creditAmount;
    private TokenTransaction.TransactionReason transactionReason;
    private UUID bookingId;
    private String subscriptionId;

    public TokenTransactionDTO() {
    }

    public TokenTransactionDTO(UUID tokenTransId, UUID userId, Long creditAmount, TokenTransaction.TransactionReason transactionReason, UUID bookingId, String subscriptionId) {
        this.tokenTransId = tokenTransId;
        this.userId = userId;
        this.creditAmount = creditAmount;
        this.transactionReason = transactionReason;
        this.bookingId = bookingId;
        this.subscriptionId = subscriptionId;
    }
}

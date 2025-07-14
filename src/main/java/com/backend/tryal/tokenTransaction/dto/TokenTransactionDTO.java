package com.backend.tryal.tokenTransaction.dto;

import com.backend.tryal.tokenTransaction.TokenTransaction;

import java.util.UUID;

public class TokenTransactionDTO {
    private UUID tokenTransId;
    private UUID userId;
    private Long creditAmount;
    private TokenTransaction.TransactionReason transactionReason;
    private UUID bookingId;
    private UUID subscriptionId;

    public TokenTransactionDTO() {
    }

    public TokenTransactionDTO(UUID tokenTransId, UUID userId, Long creditAmount, TokenTransaction.TransactionReason transactionReason, UUID bookingId, UUID subscriptionId) {
        this.tokenTransId = tokenTransId;
        this.userId = userId;
        this.creditAmount = creditAmount;
        this.transactionReason = transactionReason;
        this.bookingId = bookingId;
        this.subscriptionId = subscriptionId;
    }

    public UUID getTokenTransId() {
        return tokenTransId;
    }

    public void setTokenTransId(UUID tokenTransId) {
        this.tokenTransId = tokenTransId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Long creditAmount) {
        this.creditAmount = creditAmount;
    }

    public TokenTransaction.TransactionReason getTransactionReason() {
        return transactionReason;
    }

    public void setTransactionReason(TokenTransaction.TransactionReason transactionReason) {
        this.transactionReason = transactionReason;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}

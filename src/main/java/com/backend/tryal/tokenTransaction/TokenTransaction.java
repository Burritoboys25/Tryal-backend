package com.backend.tryal.tokenTransaction;

import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token_transactions")
public class TokenTransaction {
    public enum TransactionReason {
        BOOKING_PAYMENT,
        SUBSCRIPTION_PURCHASE,
        REFUND
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "token_trans_id")
    private UUID tokenTransId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "creditAmount", nullable = true)
    private Long creditAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transactionReason", nullable = true)
    private TokenTransaction.TransactionReason transactionReason;

    //TODO: booking_id
    //@JsonBackReference
    //@ManyToOne(fetch = FetchType.LAZY, optional = true)
    //@JoinColumn(name = "booking_id")
    //private Booking booking;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public TokenTransaction() {
    }

    public TokenTransaction(UUID tokenTransId, User user, Long creditAmount, TransactionReason transactionReason, Subscription subscription) {
        this.tokenTransId = tokenTransId;
        this.user = user;
        this.creditAmount = creditAmount;
        this.transactionReason = transactionReason;
        this.subscription = subscription;
    }

    public UUID getTokenTransId() {
        return tokenTransId;
    }

    public void setTokenTransId(UUID tokenTransId) {
        this.tokenTransId = tokenTransId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Long creditAmount) {
        this.creditAmount = creditAmount;
    }

    public TransactionReason getTransactionReason() {
        return transactionReason;
    }

    public void setTransactionReason(TransactionReason transactionReason) {
        this.transactionReason = transactionReason;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
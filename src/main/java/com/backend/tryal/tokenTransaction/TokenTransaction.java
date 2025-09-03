package com.backend.tryal.tokenTransaction;

import com.backend.tryal.booking.Booking;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public TokenTransaction() {
    }

  public TokenTransaction(UUID tokenTransId, User user, Long creditAmount,
      TransactionReason transactionReason, Booking booking, Subscription subscription) {
    this.tokenTransId = tokenTransId;
    this.user = user;
    this.creditAmount = creditAmount;
    this.transactionReason = transactionReason;
    this.booking = booking;
    this.subscription = subscription;
  }
}
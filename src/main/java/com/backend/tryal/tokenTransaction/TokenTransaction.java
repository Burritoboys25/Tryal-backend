package com.backend.tryal.tokenTransaction;

import com.backend.tryal.plan.Plan;
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

    @Column(name = "amount", nullable = true)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = true)
    private TokenTransaction.TransactionReason reason;

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
}

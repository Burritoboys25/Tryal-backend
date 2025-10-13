package com.backend.tryal.booking;

import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    public enum BookingStatus {
        BOOKED,
        CANCELLED,
        ATTENDED,
        NO_SHOW
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "booking_id")
    private UUID bookingId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = true)
    private Booking.BookingStatus bookingStatus;

    @Column(name = "party", nullable = true)
    private Integer party;


    @Column(name = "stripe_transfer_id", nullable = true)
    private String stripeTransferId;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Booking() {
    }

    public Booking(String stripeTransferId, BookingStatus bookingStatus, Timeslot timeslot, User user, UUID bookingId, Integer party) {
        this.stripeTransferId = stripeTransferId;
        this.bookingStatus = bookingStatus;
        this.timeslot = timeslot;
        this.user = user;
        this.bookingId = bookingId;
        this.party = party;
    }
}

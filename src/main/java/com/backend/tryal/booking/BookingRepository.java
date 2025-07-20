package com.backend.tryal.booking;

import com.backend.tryal.booking.dto.UserBookingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    @Query(
            value = """
        SELECT
            b.user_id AS userId,
            b.booking_id AS bookingId,
            b.timeslot_id AS timeslotId,
            e.experience_id AS experienceId,
            b.booking_status AS bookingStatus,
            bus.name AS businessName,
            bus.address AS address,
            e.credit_price AS creditPrice,
            t.timeslot_date AS timeslotDate,
            t.start_time AS startTime
        FROM bookings b
        JOIN timeslots t ON b.timeslot_id = t.timeslot_id
        JOIN experiences e ON t.experience_id = e.experience_id
        JOIN businesses bus ON e.business_id = bus.business_id
        WHERE b.user_id = :userId
        """,
            nativeQuery = true
    )
    List<UserBookingDTO> findBookingByUserId(
            @Param("userId") UUID userId
    );
}

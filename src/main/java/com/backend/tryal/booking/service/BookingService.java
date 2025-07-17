package com.backend.tryal.booking.service;

import com.backend.tryal.booking.Booking;
import com.backend.tryal.booking.dto.BookingRequestDTO;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(UUID bookingId);
    Booking createBooking(UUID userId, UUID timeslotId, BookingRequestDTO bookingRequestDTODTO);
    Booking updateBookingById(UUID bookingId, UUID timeslotId, BookingRequestDTO bookingRequestDTODTO);
    boolean deleteBookingById(UUID bookingId);
}

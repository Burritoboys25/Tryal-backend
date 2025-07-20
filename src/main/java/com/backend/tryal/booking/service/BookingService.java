package com.backend.tryal.booking.service;

import com.backend.tryal.booking.Booking;
import com.backend.tryal.booking.dto.BookingRequestDTO;
import com.backend.tryal.booking.dto.UserBookingDTO;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(UUID bookingId);
    List<UserBookingDTO> getUserBookings(UUID userId);
    Booking createBooking(UUID userId, UUID timeslotId, BookingRequestDTO bookingRequestDTODTO);
    Booking updateBookingById(UUID bookingId, UUID timeslotId, BookingRequestDTO bookingRequestDTODTO);
    boolean deleteBookingById(UUID bookingId);
}

package com.backend.tryal.booking.mapper;

import com.backend.tryal.booking.Booking;
import com.backend.tryal.booking.BookingRepository;
import com.backend.tryal.booking.dto.BookingDTO;
import com.backend.tryal.booking.dto.BookingRequestDTO;
import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.user.User;

public class BookingMapper {
    public static BookingDTO mapBookingDto(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(booking.getBookingId());

        if (booking.getUser() != null) {
            bookingDTO.setUserId(booking.getUser().getUserId());
        }

        if (booking.getTimeslot() != null) {
            bookingDTO.setTimeslotId(booking.getTimeslot().getTimeslotId());
        }

        bookingDTO.setStripeTransferId(booking.getStripeTransferId());
        bookingDTO.setBookingStatus(bookingDTO.getBookingStatus());

        return bookingDTO;
    }

    public static Booking mapRequestDTOToBooking(BookingRequestDTO bookingRequestDTO, User user, Timeslot timeslot) {
        Booking booking = new Booking();

        booking.setUser(user);
        booking.setTimeslot(timeslot);

        booking.setStripeTransferId(booking.getStripeTransferId());
        booking.setBookingStatus(booking.getBookingStatus());

        return booking;
    }
}

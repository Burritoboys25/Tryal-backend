package com.backend.tryal.booking.service;

import com.backend.tryal.booking.Booking;
import com.backend.tryal.booking.BookingRepository;
import com.backend.tryal.booking.dto.BookingRequestDTO;
import com.backend.tryal.booking.dto.UserBookingDTO;
import com.backend.tryal.booking.mapper.BookingMapper;
import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.timeslot.TimeslotRepository;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimeslotRepository timeslotRepository;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    public List<UserBookingDTO> getUserBookings(UUID userId) {
        return bookingRepository.findBookingByUserId(userId);
    }

    @Override
    public Booking createBooking(UUID userId, UUID timeslotId, BookingRequestDTO bookingRequestDTO) {
        User user = userRepository.findById(userId).orElse(null);
        Timeslot timeslot = timeslotRepository.findById(timeslotId).orElse(null);

        if (user == null || timeslot == null) {
            return null;
        }

        Booking booking = BookingMapper.mapRequestDTOToBooking(bookingRequestDTO, user, timeslot);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBookingById(UUID bookingId, UUID timeslotId, BookingRequestDTO bookingRequestDTO) {
        if (getBookingById(bookingId) != null) {

            Booking updatedBooking = getBookingById(bookingId);

            timeslotRepository.findById(timeslotId).ifPresent(updatedBooking::setTimeslot);

            if (bookingRequestDTO.getStripeTransferId() != null) {
                updatedBooking.setStripeTransferId(bookingRequestDTO.getStripeTransferId());
            }

            if (bookingRequestDTO.getBookingStatus() != null) {
                updatedBooking.setBookingStatus(Booking.BookingStatus.valueOf(bookingRequestDTO.getBookingStatus()));
            }

            bookingRepository.save(updatedBooking);
            return updatedBooking;
        }
        return null;
    }

    @Override
    public boolean deleteBookingById(UUID bookingId) {
        if (getBookingById(bookingId) != null) {
            bookingRepository.deleteById(bookingId);
            return true;
        }

        return false;
    }

}

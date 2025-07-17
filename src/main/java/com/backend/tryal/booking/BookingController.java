package com.backend.tryal.booking;

import com.backend.tryal.booking.dto.BookingDTO;
import com.backend.tryal.booking.mapper.BookingMapper;
import com.backend.tryal.booking.service.BookingService;
import com.backend.tryal.booking.response.BookingResponse;
import com.backend.tryal.booking.dto.BookingRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    BookingService bookingService;

    // get all bookings
    @GetMapping()
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        try {
            List<BookingDTO> bookings = bookingService.getAllBookings()
                    .stream()
                    .map(BookingMapper::mapBookingDto)
                    .collect(Collectors.toList());

            if (bookings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get booking by ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable UUID bookingId) {
        try {
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking == null) {
                return new ResponseEntity<>(new BookingResponse(null, "Booking not found."),HttpStatus.NOT_FOUND);
            }

            BookingDTO bookingDTO = BookingMapper.mapBookingDto(booking);

            return new ResponseEntity<>(new BookingResponse(bookingDTO, "booking found."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // create booking
    @PostMapping()
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO, @RequestParam UUID userId, @RequestParam UUID bookingId) {
        try {
            BookingDTO bookingDTO = BookingMapper.mapBookingDto(bookingService.createBooking(userId, bookingId, bookingRequestDTO));

            if(bookingDTO == null){
                return new ResponseEntity<>(new BookingResponse(null, "User or booking not found."), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new BookingResponse(bookingDTO, "Booking created successfully."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // patch booking
    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> updateBookingById(@RequestBody BookingRequestDTO bookingRequestDTO, @PathVariable UUID bookingId, @RequestParam UUID timeslotId) {
        try {
            Booking updatedBooking = bookingService.updateBookingById(bookingId, timeslotId, bookingRequestDTO);

            if(updatedBooking == null){
                return new ResponseEntity<>(new BookingResponse(null, "Booking not found."), HttpStatus.NOT_FOUND);
            }

            BookingDTO bookingDTO = BookingMapper.mapBookingDto(updatedBooking);

            return new ResponseEntity<>(new BookingResponse(bookingDTO, "Booking updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete booking
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBookingById(@PathVariable UUID bookingId) {
        try {

            if (bookingService.deleteBookingById(bookingId)) {
                return new ResponseEntity<>("Booking deleted successfully.", HttpStatus.OK);
            }

            return new ResponseEntity<>("Booking not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

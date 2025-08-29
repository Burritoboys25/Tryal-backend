package com.backend.tryal.booking;

import com.backend.tryal.booking.dto.BookingDTO;
import com.backend.tryal.booking.dto.UserBookingDTO;
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
    public List<BookingDTO> getAllBookings() {
            return bookingService.getAllBookings()
                    .stream()
                    .map(BookingMapper::mapBookingDto)
                    .collect(Collectors.toList());
    }

    // get booking by ID
    @GetMapping("/{bookingId}")
    public BookingDTO getBookingById(@PathVariable UUID bookingId) {
            Booking booking = bookingService.getBookingById(bookingId);

            return BookingMapper.mapBookingDto(booking);
    }

    // get booking by user ID
    @GetMapping("/user/{userId}")
    public List<UserBookingDTO> getUserBookings(@PathVariable UUID userId) {
            return bookingService.getUserBookings(userId);

    }

    // create booking
    @PostMapping()
    public BookingDTO createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
            return BookingMapper.mapBookingDto(bookingService.createBooking(bookingRequestDTO));
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

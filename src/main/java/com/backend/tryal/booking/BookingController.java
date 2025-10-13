package com.backend.tryal.booking;

import com.backend.tryal.booking.dto.BookingDTO;
import com.backend.tryal.booking.dto.BookingPatchDTO;
import com.backend.tryal.booking.dto.BookingRequestDTO;
import com.backend.tryal.booking.dto.UserBookingDTO;
import com.backend.tryal.booking.mapper.BookingMapper;
import com.backend.tryal.booking.service.BookingService;
import com.backend.tryal.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public BookingDTO updateBookingById(@RequestBody BookingPatchDTO bookingRequestDTO,
      @PathVariable UUID bookingId) {
    Booking updatedBooking = bookingService.updateBookingById(bookingId, bookingRequestDTO);

    return BookingMapper.mapBookingDto(updatedBooking);
  }

  // delete booking
  @DeleteMapping("/{bookingId}")
  public ApiResponse<String> deleteBookingById(@PathVariable UUID bookingId) {
    bookingService.deleteBookingById(bookingId);
    return new ApiResponse<>("success", "Booking deleted successfully.");
  }
}

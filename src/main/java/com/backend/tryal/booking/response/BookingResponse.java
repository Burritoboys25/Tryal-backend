package com.backend.tryal.booking.response;

import com.backend.tryal.booking.dto.BookingDTO;
import lombok.Data;

@Data
public class BookingResponse {
    private BookingDTO bookingDTO;
    private String message;

    public BookingResponse(BookingDTO bookingDTO, String message) {
        this.bookingDTO = bookingDTO;
        this.message = message;
    }
}

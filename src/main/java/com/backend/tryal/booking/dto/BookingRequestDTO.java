package com.backend.tryal.booking.dto;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private String stripeTransferId;
    private String bookingStatus;
}

package com.backend.tryal.booking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingDTO {
    private UUID bookingId;
    private UUID userId;
    private UUID timeslotId;
    private String stripeTransferId;
    private String bookingStatus;
}

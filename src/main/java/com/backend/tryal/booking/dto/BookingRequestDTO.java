package com.backend.tryal.booking.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class BookingRequestDTO {
    private UUID userId;
    private UUID timeslotId;
    private String stripeTransferId;
    private String bookingStatus;
    private Integer party;
}

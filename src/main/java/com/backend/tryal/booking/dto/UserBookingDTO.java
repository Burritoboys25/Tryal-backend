package com.backend.tryal.booking.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class UserBookingDTO {
    private UUID userId;
    private UUID bookingId;
    private UUID timeslotId;
    private UUID experienceId;
    private String businessName;
    private String address;
    private Integer creditPrice;
    private Integer party;
    private LocalDate timeslotDate;
    private LocalTime startTime;
}

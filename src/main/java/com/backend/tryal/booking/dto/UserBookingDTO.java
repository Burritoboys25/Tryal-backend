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

    public UserBookingDTO(UUID userId, UUID bookingId, UUID timeslotId, UUID experienceId, String businessName, String address, Integer creditPrice, Integer party, LocalDate timeslotDate, LocalTime startTime) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.timeslotId = timeslotId;
        this.experienceId = experienceId;
        this.businessName = businessName;
        this.address = address;
        this.creditPrice = creditPrice;
        this.party = party;
        this.timeslotDate = timeslotDate;
        this.startTime = startTime;
    }
}

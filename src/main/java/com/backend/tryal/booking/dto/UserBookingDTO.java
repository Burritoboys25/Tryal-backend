package com.backend.tryal.booking.dto;

import lombok.Data;

import java.sql.Time;
import java.sql.Date;
import java.util.UUID;

@Data
public class UserBookingDTO {
    private UUID userId;
    private UUID bookingId;
    private UUID timeslotId;
    private UUID experienceId;
    private String bookingStatus;
    private String businessName;
    private String address;
    private Integer creditPrice;
    private Integer party;
    private Date timeslotDate;
    private Time startTime;

    public UserBookingDTO(UUID userId, UUID bookingId, UUID timeslotId, UUID experienceId, String bookingStatus, String businessName, String address, Integer creditPrice, Integer party, Date timeslotDate, Time startTime) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.timeslotId = timeslotId;
        this.experienceId = experienceId;
        this.bookingStatus = bookingStatus;
        this.businessName = businessName;
        this.address = address;
        this.creditPrice = creditPrice;
        this.party = party;
        this.timeslotDate = timeslotDate;
        this.startTime = startTime;
    }
}

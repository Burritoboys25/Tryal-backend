package com.backend.tryal.timeslot.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
public class TimeslotDTO {
    private UUID timeslotId;
    private UUID experienceId;
    private LocalDate timeslotDate;
    private Time startTime;
    private Boolean isCancelled;
    private Double expConvertPrice;

//  public TimeslotDTO() {
//  }
//
//  public TimeslotDTO(UUID timeslotId, UUID experienceId, LocalDate timeslotDate, Time startTime,
//      Boolean isCancelled, Double expConvertPrice) {
//    this.timeslotId = timeslotId;
//    this.experienceId = experienceId;
//    this.timeslotDate = timeslotDate;
//    this.startTime = startTime;
//    this.isCancelled = isCancelled;
//    this.expConvertPrice = expConvertPrice;
//  }
}

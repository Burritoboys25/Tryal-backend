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
}

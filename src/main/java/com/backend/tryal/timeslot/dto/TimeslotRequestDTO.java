package com.backend.tryal.timeslot.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

import java.sql.Time;

@Data
public class TimeslotRequestDTO {
    private UUID experienceId;
    private LocalDate timeslotDate;
    private Time startTime;
    private Boolean isCancelled;
    private Double expConvertPrice;
}

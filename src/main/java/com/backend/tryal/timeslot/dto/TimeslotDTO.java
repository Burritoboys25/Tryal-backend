package com.backend.tryal.timeslot.dto;

import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Data
public class TimeslotDTO {
    private UUID timeslotId;
    private UUID experienceId;
    private Date timeslotDate;
    private Time startTime;
    private Boolean isCancelled;
    private Double expConvertPrice;
}

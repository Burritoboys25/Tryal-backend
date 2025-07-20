package com.backend.tryal.timeslot.dto;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class TimeslotRequestDTO {
    private Date timeslotDate;
    private Time startTime;
    private Boolean isCancelled;
    private Double expConvertPrice;
}

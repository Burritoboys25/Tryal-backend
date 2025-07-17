package com.backend.tryal.timeslot.response;

import com.backend.tryal.timeslot.dto.TimeslotDTO;
import lombok.Data;

@Data
public class TimeslotResponse {
    private TimeslotDTO timeslotDTO;
    private String message;

    public TimeslotResponse(TimeslotDTO timeslotDTO, String message) {
        this.timeslotDTO = timeslotDTO;
        this.message = message;
    }
}

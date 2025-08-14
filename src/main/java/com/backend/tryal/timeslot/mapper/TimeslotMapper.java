package com.backend.tryal.timeslot.mapper;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import com.backend.tryal.timeslot.dto.TimeslotRequestDTO;

import java.sql.Date;

public class TimeslotMapper {
    public static TimeslotDTO mapTimeslotDto(Timeslot timeslot) {
        TimeslotDTO timeslotDTO = new TimeslotDTO();
        timeslotDTO.setTimeslotId(timeslot.getTimeslotId());

        if (timeslot.getExperience() != null) {
            timeslotDTO.setExperienceId(timeslot.getExperience().getExperienceId());
        }

        timeslotDTO.setTimeslotDate(timeslot.getTimeslotDate());
        timeslotDTO.setStartTime(timeslot.getStartTime());
        timeslotDTO.setIsCancelled(timeslot.getIsCancelled());
        timeslotDTO.setExpConvertPrice(timeslot.getExpConvertPrice());

        return timeslotDTO;
    }

    public  static Timeslot mapRequestDTOToTimeslot(TimeslotRequestDTO timeslotRequestDTO, Experience experience) {
        Timeslot timeslot = new Timeslot();

        timeslot.setExperience(experience);

        timeslot.setTimeslotDate((Date) timeslotRequestDTO.getTimeslotDate());
        timeslot.setStartTime(timeslotRequestDTO.getStartTime());
        timeslot.setIsCancelled(timeslot.getIsCancelled());
        timeslot.setExpConvertPrice(timeslotRequestDTO.getExpConvertPrice());

        return timeslot;
    }
}

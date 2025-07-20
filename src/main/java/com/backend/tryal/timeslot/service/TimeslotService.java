package com.backend.tryal.timeslot.service;

import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.timeslot.dto.TimeslotRequestDTO;

import java.util.List;
import java.util.UUID;

public interface TimeslotService {
    List<Timeslot> getAllTimeslots();
    Timeslot getTimeslotById(UUID timeslotId);
    Timeslot createTimeslot(UUID experienceId, TimeslotRequestDTO timeslotRequestDTO);
    Timeslot updateTimeslotById(UUID timeslotId, TimeslotRequestDTO timeslotRequestDTO);
    boolean deleteTimeslotById(UUID timeslotId);
}

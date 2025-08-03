package com.backend.tryal.timeslot.service;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.ExperienceRepository;
import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.timeslot.TimeslotRepository;
import com.backend.tryal.timeslot.dto.TimeslotRequestDTO;
import com.backend.tryal.timeslot.mapper.TimeslotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TimeslotServiceImpl implements TimeslotService {
    @Autowired
    TimeslotRepository timeslotRepository;

    @Autowired
    ExperienceRepository experienceRepository;

    @Override
    public List<Timeslot> getAllTimeslots() {
        return timeslotRepository.findAll();
    }

    @Override
    public Timeslot getTimeslotById(UUID timeslotId) {
        return timeslotRepository.findById(timeslotId).orElse(null);
    }

    @Override
    public Timeslot createTimeslot(UUID experienceId, TimeslotRequestDTO timeslotRequestDTO) {
        Experience experience = experienceRepository.findById(experienceId).orElse(null);

        if (experience == null) {
            return null;
        }

        Timeslot timeslot = TimeslotMapper.mapRequestDTOToTimeslot(timeslotRequestDTO, experience);

        return timeslotRepository.save(timeslot);
    }

    @Override
    public Timeslot updateTimeslotById(UUID timeslotId, TimeslotRequestDTO timeslotRequestDTO) {
        if (getTimeslotById(timeslotId) != null) {
            Timeslot updatedTimeslot = getTimeslotById(timeslotId);

            if (timeslotRequestDTO.getTimeslotDate() != null) {
                updatedTimeslot.setTimeslotDate((Date) timeslotRequestDTO.getTimeslotDate());
            }

            if (timeslotRequestDTO.getStartTime() != null) {
                updatedTimeslot.setStartTime(timeslotRequestDTO.getStartTime());
            }

            if (timeslotRequestDTO.getIsCancelled() != null) {
                updatedTimeslot.setIsCancelled(timeslotRequestDTO.getIsCancelled());
            }

            if (timeslotRequestDTO.getExpConvertPrice() != null) {
                updatedTimeslot.setExpConvertPrice(timeslotRequestDTO.getExpConvertPrice());
            }

            timeslotRepository.save(updatedTimeslot);
            return updatedTimeslot;
        }
        return null;
    }

    @Override
    public boolean deleteTimeslotById(UUID timeslotId) {
        if (getTimeslotById(timeslotId) != null) {
            timeslotRepository.deleteById(timeslotId);
            return true;
        }

        return false;
    }
}

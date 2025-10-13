package com.backend.tryal.timeslot.service;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.ExperienceRepository;
import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.timeslot.TimeslotRepository;
import com.backend.tryal.timeslot.dto.TimeslotRequestDTO;
import com.backend.tryal.timeslot.mapper.TimeslotMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    Timeslot timeslot = timeslotRepository.findById(timeslotId).orElse(null);
    if (timeslot == null) {
      throw new EntityNotFoundException("Timeslot not found with id " + timeslotId);
    }
    return timeslot;
  }

  @Override
  public Timeslot createTimeslot(TimeslotRequestDTO timeslotRequestDTO) {
    Experience experience = experienceRepository.findById(timeslotRequestDTO.getExperienceId())
        .orElse(null);
    if (experience == null) {
      throw new IllegalArgumentException(
          "Experience not found with id " + timeslotRequestDTO.getExperienceId());
    }

    Timeslot timeslot = TimeslotMapper.mapRequestDTOToTimeslot(timeslotRequestDTO, experience);

    return timeslotRepository.save(timeslot);
  }

  @Override
  public Timeslot updateTimeslotById(UUID timeslotId, TimeslotRequestDTO timeslotRequestDTO) {
    Timeslot updatedTimeslot = getTimeslotById(timeslotId);

    if (updatedTimeslot == null) {
      throw new EntityNotFoundException("Timeslot not found with id " + timeslotId);
    }

    if (timeslotRequestDTO.getTimeslotDate() != null) {
      updatedTimeslot.setTimeslotDate(timeslotRequestDTO.getTimeslotDate());
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

  @Override
  public void deleteTimeslotById(UUID timeslotId) {
    if (getTimeslotById(timeslotId) == null) {
      throw new EntityNotFoundException("Timeslot not found with id " + timeslotId);
    }
    timeslotRepository.deleteById(timeslotId);
  }
}

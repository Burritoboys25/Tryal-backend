package com.backend.tryal.experience.service;

import static java.util.stream.Collectors.toList;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.ExperienceRepository;
import com.backend.tryal.experience.dto.BusinessExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.experience.mapper.ExperienceMapper;
import com.backend.tryal.timeslot.Timeslot;
import com.backend.tryal.timeslot.TimeslotRepository;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import com.backend.tryal.timeslot.mapper.TimeslotMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExperienceServiceImpl implements ExperienceService {

  private final ExperienceRepository experienceRepository;
  private final BusinessRepository businessRepository;

  @Autowired
  private TimeslotRepository timeslotRepository;

  public ExperienceServiceImpl(ExperienceRepository experienceRepository,
      BusinessRepository businessRepository) {
    this.experienceRepository = experienceRepository;
    this.businessRepository = businessRepository;
  }

  @Override
  public List<Experience> getAllExperiences() {
    return experienceRepository.findAll();
  }

  @Override
  public List<Experience> getExperiencesByBusinessId_TEST(UUID businessId) {
    if (businessRepository.findById(businessId).orElse(null) == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }
    return experienceRepository.findByBusiness_BusinessId(businessId);
  }

  @Override
  public List<BusinessExperienceDTO> getExperiencesByBusinessId(UUID businessId) {
    if (businessRepository.findById(businessId).orElse(null) == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }

    List<BusinessExperienceDTO> experiences =
        experienceRepository.findByBusiness_BusinessId(businessId)
            .stream()
            .map(ExperienceMapper::mapBusinessExperiencesDto)
            .toList();
    System.out.println("1:");
    List<TimeslotDTO> timeslots = timeslotRepository.findByBusinessId(businessId)
            .stream()
                .map(TimeslotMapper::mapTimeslotDto)
                    .toList();

    for (BusinessExperienceDTO businessExperienceDTO: experiences) {
      List<TimeslotDTO> filteredTimeslots = new ArrayList<>();

      for (TimeslotDTO timeslotDTO : timeslots) {
        if (timeslotDTO.getExperienceId() == businessExperienceDTO.getExperienceId()) {
          filteredTimeslots.add(timeslotDTO);
        }
      }
      businessExperienceDTO.setTimeslots(filteredTimeslots);
    }

    return experiences;
  }

  @Override
  public Experience getExperienceById(UUID experienceId) {
    Experience experience = experienceRepository.findById(experienceId).orElse(null);
    if (experience == null) {
      throw new EntityNotFoundException("Could not find experience with id: " + experienceId);
    }
    return experience;
  }

  @Override
  public Experience createExperience(ExperienceRequestDTO experienceRequestDTO) {
    UUID businessId = experienceRequestDTO.getBusinessId();
    Business business = businessRepository.findById(businessId).orElse(null);

    if (business == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }
    Experience experience = ExperienceMapper.mapRequestDTOToExperience(experienceRequestDTO,
        business);

    return experienceRepository.save(experience);
  }

  @Override
  public Experience updateExperienceById(ExperienceRequestDTO experienceRequestDTO,
      UUID experienceId) {
    if (getExperienceById(experienceId) == null) {
      throw new EntityNotFoundException("Could not find experience with id: " + experienceId);
    }

    Experience updatedExperience = getExperienceById(experienceId);

    if (experienceRequestDTO.getExperienceName() != null) {
      updatedExperience.setExperienceName(experienceRequestDTO.getExperienceName());
    }

    if (experienceRequestDTO.getDescription() != null) {
      updatedExperience.setDescription(experienceRequestDTO.getDescription());
    }

    if (experienceRequestDTO.getSkillLevel() != null) {
      updatedExperience.setSkillLevel(experienceRequestDTO.getSkillLevel());
    }

    if (experienceRequestDTO.getMaxCapacity() != null) {
      updatedExperience.setMaxCapacity(experienceRequestDTO.getMaxCapacity());
    }

    if (experienceRequestDTO.getRemainingCapacity() != null) {
      updatedExperience.setMaxCapacity(experienceRequestDTO.getRemainingCapacity());
    }

    if (experienceRequestDTO.getDuration() != null) {
      updatedExperience.setDuration(experienceRequestDTO.getDuration());
    }

    if (experienceRequestDTO.getCreditPrice() != null) {
      updatedExperience.setCreditPrice(experienceRequestDTO.getCreditPrice());
    }

    if (experienceRequestDTO.getIsActive() != null) {
      updatedExperience.setIsActive(experienceRequestDTO.getIsActive());
    }

    return experienceRepository.save(updatedExperience);
  }

  @Override
  public void deleteExperienceById(UUID experienceId) {
    if (getExperienceById(experienceId) == null) {
      throw new EntityNotFoundException("Could not find experience with id: " + experienceId);
    }
    experienceRepository.deleteById(experienceId);
  }


}

package com.backend.tryal.experience.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.ExperienceRepository;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.experience.mapper.ExperienceMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ExperienceServiceImpl implements ExperienceService {

  private final ExperienceRepository experienceRepository;
  private final BusinessRepository businessRepository;

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
  public List<Experience> getExperiencesByBusinessId(UUID businessId) {
    if (businessRepository.findById(businessId).orElse(null) == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }
    return experienceRepository.findByBusiness_BusinessId(businessId);
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

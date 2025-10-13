package com.backend.tryal.experience.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.dto.BusinessExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import java.util.ArrayList;
import java.util.List;

public class ExperienceMapper {

    public static ExperienceDTO mapExperienceDto(Experience experience){
        ExperienceDTO experienceDTO = new ExperienceDTO();
        experienceDTO.setExperienceId(experience.getExperienceId());

        if (experience.getBusiness() != null) {
            experienceDTO.setBusinessId(experience.getBusiness().getBusinessId());
        }

        experienceDTO.setExperienceName(experience.getExperienceName());
        experienceDTO.setDescription(experience.getDescription());
        experienceDTO.setSkillLevel(experience.getSkillLevel());
        experienceDTO.setMaxCapacity(experience.getMaxCapacity());
        experienceDTO.setRemainingCapacity(experience.getRemainingCapacity());
        experienceDTO.setDuration(experience.getDuration());
        experienceDTO.setCreditPrice(experience.getCreditPrice());
        experienceDTO.setIsActive(experience.getIsActive());

        return experienceDTO;
    }

    public static BusinessExperienceDTO mapBusinessExperiencesDto(Experience experience) {
      BusinessExperienceDTO businessExperienceDTO = new BusinessExperienceDTO();

      businessExperienceDTO.setExperienceId(experience.getExperienceId());

      if (experience.getBusiness() != null) {
        businessExperienceDTO.setBusinessId(experience.getBusiness().getBusinessId());
      }

      businessExperienceDTO.setExperienceName(experience.getExperienceName());
      businessExperienceDTO.setDescription(experience.getDescription());
      businessExperienceDTO.setSkillLevel(experience.getSkillLevel());
      businessExperienceDTO.setMaxCapacity(experience.getMaxCapacity());
      businessExperienceDTO.setRemainingCapacity(experience.getRemainingCapacity());
      businessExperienceDTO.setDuration(experience.getDuration());
      businessExperienceDTO.setCreditPrice(experience.getCreditPrice());
      businessExperienceDTO.setIsActive(experience.getIsActive());
      return businessExperienceDTO;
    }

    public static Experience mapRequestDTOToExperience(ExperienceRequestDTO experienceDTO, Business business){
        Experience experience = new Experience();

        experience.setBusiness(business);

        experience.setExperienceName(experienceDTO.getExperienceName());
        experience.setDescription(experienceDTO.getDescription());
        experience.setSkillLevel(experienceDTO.getSkillLevel());
        experience.setMaxCapacity(experience.getMaxCapacity());
        experience.setRemainingCapacity(experience.getRemainingCapacity());
        experience.setDuration(experienceDTO.getDuration());
        experience.setCreditPrice(experienceDTO.getCreditPrice());
        experience.setIsActive(experienceDTO.getIsActive());

        return experience;
    }
}

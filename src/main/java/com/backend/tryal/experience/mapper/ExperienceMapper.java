package com.backend.tryal.experience.mapper;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.dto.ExperienceDTO;

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
        experienceDTO.setCapacity(experience.getCapacity());
        experienceDTO.setDuration(experience.getDuration());
        experienceDTO.setCreditPrice(experience.getCreditPrice());
        experienceDTO.setIsActive(experience.getIsActive());

        return experienceDTO;
    }
}

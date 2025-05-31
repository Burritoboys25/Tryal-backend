package com.backend.tryal.experience.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.dto.ExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;

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
        experienceDTO.setActive(experience.getActive());

        return experienceDTO;
    }

    public static Experience mapRequestDTOToExperience(ExperienceRequestDTO experienceDTO, Business business){
        Experience experience = new Experience();

        experience.setBusiness(business);

        experience.setExperienceName(experienceDTO.getExperienceName());
        experience.setDescription(experienceDTO.getDescription());
        experience.setSkillLevel(experienceDTO.getSkillLevel());
        experience.setCapacity(experienceDTO.getCapacity());
        experience.setDuration(experienceDTO.getDuration());
        experience.setCreditPrice(experienceDTO.getCreditPrice());
        experience.setActive(experienceDTO.getActive());

        return experience;
    }
}

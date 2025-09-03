package com.backend.tryal.experience.service;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;

import java.util.List;
import java.util.UUID;

public interface ExperienceService {
    List<Experience> getAllExperiences();
    Experience getExperienceById(UUID experienceId);
    Experience createExperience(ExperienceRequestDTO experienceRequestDTO);
    Experience updateExperienceById(ExperienceRequestDTO experienceRequestDTO, UUID experienceId);
    void deleteExperienceById(UUID experienceId);
    List<Experience> getExperiencesByBusinessId(UUID businessId);
}

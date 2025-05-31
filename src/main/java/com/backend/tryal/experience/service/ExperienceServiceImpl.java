package com.backend.tryal.experience.service;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;

import java.util.List;
import java.util.UUID;

public class ExperienceServiceImpl implements ExperienceService{
    @Override
    public List<Experience> getAllExperiences() {
        return null;
    }

    @Override
    public Experience getExperienceById(UUID experienceId) {
        return null;
    }

    @Override
    public Experience createExperience(ExperienceRequestDTO experienceRequestDTO, UUID businessId) {
        return null;
    }

    @Override
    public Experience updateExperienceById(ExperienceRequestDTO experienceRequestDTO, UUID experienceId) {
        return null;
    }

    @Override
    public boolean deleteExperienceById(UUID experienceId) {
        return false;
    }
}

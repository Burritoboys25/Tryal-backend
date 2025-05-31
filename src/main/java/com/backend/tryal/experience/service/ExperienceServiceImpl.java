package com.backend.tryal.experience.service;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.ExperienceRepository;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExperienceServiceImpl implements ExperienceService{
    private final ExperienceRepository experienceRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    @Override
    public Experience getExperienceById(UUID experienceId) {
        return experienceRepository.findById(experienceId).orElse(null);
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
        if (getExperienceById(experienceId) != null) {
            experienceRepository.deleteById(experienceId);
            return true;
        }

        return false;
    }
}

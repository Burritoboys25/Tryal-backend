package com.backend.tryal.experience.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.experience.ExperienceRepository;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.experience.mapper.ExperienceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExperienceServiceImpl implements ExperienceService{
    private final ExperienceRepository experienceRepository;
    private final BusinessRepository businessRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, BusinessRepository businessRepository) {
        this.experienceRepository = experienceRepository;
        this.businessRepository = businessRepository;
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
        Business business = businessRepository.findById(businessId).orElse(null);

        if(business == null){
            return null;
        }

        Experience experience = ExperienceMapper.mapRequestDTOToExperience(experienceRequestDTO, business);

        return experienceRepository.save(experience);
    }

    @Override
    public Experience updateExperienceById(ExperienceRequestDTO experienceRequestDTO, UUID experienceId) {
        if(getExperienceById(experienceId) != null){
            Experience updatedExperience = getExperienceById(experienceId);

            if(experienceRequestDTO.getExperienceName() != null){
                updatedExperience.setExperienceName(experienceRequestDTO.getExperienceName());
            }

            if(experienceRequestDTO.getDescription() != null){
                updatedExperience.setDescription(experienceRequestDTO.getDescription());
            }

            if(experienceRequestDTO.getSkillLevel() != null){
                updatedExperience.setSkillLevel(experienceRequestDTO.getSkillLevel());
            }

            if(experienceRequestDTO.getCapacity() != null){
                updatedExperience.setCapacity(experienceRequestDTO.getCapacity());
            }

            if(experienceRequestDTO.getDuration() != null){
                updatedExperience.setDuration(experienceRequestDTO.getDuration());
            }

            if(experienceRequestDTO.getCreditPrice() != null){
                updatedExperience.setCreditPrice(experienceRequestDTO.getCreditPrice());
            }

            if(experienceRequestDTO.getActive() != null){
                updatedExperience.setActive(experienceRequestDTO.getActive());
            }

            return experienceRepository.save(updatedExperience);
        }

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

package com.backend.tryal.experience.dto;

import com.backend.tryal.experience.Experience;
import lombok.Data;

import java.util.UUID;

@Data
public class ExperienceDTO {
    private UUID experienceId;
    private UUID businessId;
    private String experienceName;
    private String description;
    private Experience.SkillLevel skillLevel;
    private Integer capacity;
    private Integer duration;
    private Integer creditPrice;
    private Boolean isActive;

    public ExperienceDTO() {
    }

    public ExperienceDTO(UUID experienceId, UUID businessId, String experienceName, String description, Experience.SkillLevel skillLevel, Integer capacity, Integer duration, Integer creditPrice, Boolean isActive) {
        this.experienceId = experienceId;
        this.businessId = businessId;
        this.experienceName = experienceName;
        this.description = description;
        this.skillLevel = skillLevel;
        this.capacity = capacity;
        this.duration = duration;
        this.creditPrice = creditPrice;
        this.isActive = isActive;
    }
}

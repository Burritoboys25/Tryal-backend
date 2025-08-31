package com.backend.tryal.experience.dto;

import com.backend.tryal.experience.Experience;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;

@Data
public class ExperienceRequestDTO {
    @NotBlank(message = "Business id is required")
    private UUID businessId;
    @NotBlank(message = "Experience name is required")
    private String experienceName;
    private String description;
    private Experience.SkillLevel skillLevel;
    private Integer maxCapacity;
    private Integer remainingCapacity;
    private Integer duration;
    private Integer creditPrice;
    private Boolean isActive;

    public ExperienceRequestDTO() {
    }

    public ExperienceRequestDTO(UUID businessId, String experienceName, String description, Experience.SkillLevel skillLevel, Integer maxCapacity, Integer remainingCapacity, Integer duration, Integer creditPrice, Boolean isActive) {
        this.experienceName = experienceName;
        this.description = description;
        this.skillLevel = skillLevel;
        this.maxCapacity = maxCapacity;
        this.remainingCapacity = remainingCapacity;
        this.duration = duration;
        this.creditPrice = creditPrice;
        this.isActive = isActive;
    }
}

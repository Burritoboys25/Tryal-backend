package com.backend.tryal.experience.dto;

import com.backend.tryal.experience.Experience;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExperienceRequestDTO {

    @NotBlank(message = "Experience name is required")
    private String experienceName;
    private String description;
    private Experience.SkillLevel skillLevel;
    private Integer capacity;
    private Integer duration;
    private Integer creditPrice;
    private Boolean isActive;

    public ExperienceRequestDTO() {
    }

    public ExperienceRequestDTO(String experienceName, String description, Experience.SkillLevel skillLevel, Integer capacity, Integer duration, Integer creditPrice, Boolean isActive) {
        this.experienceName = experienceName;
        this.description = description;
        this.skillLevel = skillLevel;
        this.capacity = capacity;
        this.duration = duration;
        this.creditPrice = creditPrice;
        this.isActive = isActive;
    }
}

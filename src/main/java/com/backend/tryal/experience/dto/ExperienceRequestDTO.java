package com.backend.tryal.experience.dto;

import com.backend.tryal.experience.Experience;
import jakarta.validation.constraints.NotBlank;

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

    public String getExperienceName() {
        return experienceName;
    }

    public void setExperienceName(String experienceName) {
        this.experienceName = experienceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Experience.SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Experience.SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCreditPrice() {
        return creditPrice;
    }

    public void setCreditPrice(Integer creditPrice) {
        this.creditPrice = creditPrice;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

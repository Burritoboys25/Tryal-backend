package com.backend.tryal.experience.dto;

import com.backend.tryal.experience.Experience;

import java.util.UUID;

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

    public UUID getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(UUID experienceId) {
        this.experienceId = experienceId;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}

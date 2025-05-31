package com.backend.tryal.experience;

import com.backend.tryal.business.Business;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "experiences")
public class Experience {
    public enum SkillLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "experience_id")
    private UUID experienceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "experience_name", nullable = true)
    private String experienceName;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "skill_level", nullable = true)
    private SkillLevel skillLevel;

    @Column(name = "capacity", nullable = true)
    private Integer capacity;

    @Column(name = "duration", nullable = true)
    private Integer duration;

    @Column(name = "credit_price", nullable = true)
    private Integer creditPrice;

    @Column(name = "is_active", nullable = true)
    private Boolean isActive;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Experience() {
    }

    public Experience(UUID experienceId, Business business, String experienceName, String description, SkillLevel skillLevel, Integer capacity, Integer duration, Integer creditPrice, Boolean isActive) {
        this.experienceId = experienceId;
        this.business = business;
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

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

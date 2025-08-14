package com.backend.tryal.experience;

import com.backend.tryal.business.Business;
import com.backend.tryal.category.Category;
import com.backend.tryal.groupType.GroupType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "experiences")
public class Experience {
    public enum SkillLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "experience_id")
    private UUID experienceId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "experience_name", nullable = true)
    private String experienceName;

    @Column(name = "description", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level", nullable = true)
    private SkillLevel skillLevel;

    @Column(name = "max_capacity", nullable = true)
    private Integer maxCapacity;

    @Column(name = "remaining_capacity", nullable = true)
    private Integer remainingCapacity;

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

    @ManyToMany
    @JoinTable(
            name = "experience_categories",
            joinColumns = @JoinColumn(name = "experience_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "experience_group_types",
            joinColumns = @JoinColumn(name = "experience_id"),
            inverseJoinColumns = @JoinColumn(name = "group_type_id")
    )
    private Set<GroupType> groupTypes = new HashSet<>();

    public Experience() {
    }

    public Experience(UUID experienceId, Business business, String experienceName, String description, SkillLevel skillLevel, Integer maxCapacity, Integer remainingCapacity, Integer duration, Integer creditPrice, Boolean isActive, Set<Category> categories, Set<GroupType> groupTypes) {
        this.experienceId = experienceId;
        this.business = business;
        this.experienceName = experienceName;
        this.description = description;
        this.skillLevel = skillLevel;
        this.maxCapacity = maxCapacity;
        this.remainingCapacity = remainingCapacity;
        this.duration = duration;
        this.creditPrice = creditPrice;
        this.isActive = isActive;
        this.categories = categories;
        this.groupTypes = groupTypes;
    }
}

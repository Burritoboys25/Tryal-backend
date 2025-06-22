package com.backend.tryal.business.dto;

import com.backend.tryal.experience.Experience;
import lombok.Data;

import java.util.List;

// Response pay load of filtered businesses
@Data
public class BusinessFilteredRequestDTO {
    private List<Long> categoryIds;
    private Integer groupTypeIds;
    private Integer duration;
    private List<Experience.SkillLevel> skillLevel;
    private Integer creditsMin;
    private Integer creditsMax;
    private Integer limit;
}

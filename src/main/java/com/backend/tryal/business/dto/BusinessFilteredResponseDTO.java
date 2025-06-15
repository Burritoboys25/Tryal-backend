package com.backend.tryal.business.dto;

import com.backend.tryal.category.dto.FilteredCategoryDTO;
import com.backend.tryal.experience.Experience;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BusinessFilteredResponseDTO {
    private UUID businessId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    List<FilteredExperience> filteredExperiences;

    @Data
    public static class FilteredExperience {
        private Integer duration;
        private Experience.SkillLevel skillLevel;
        private List<FilteredCategoryDTO> categories;
    }
}


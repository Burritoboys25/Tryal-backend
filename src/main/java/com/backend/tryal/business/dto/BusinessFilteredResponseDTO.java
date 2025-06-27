package com.backend.tryal.business.dto;

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
    private List<String> categories;
    private List<Experience.SkillLevel> skillLevels;
    private Integer minCredits;
    private Integer maxCredits;
}

package com.backend.tryal.business.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessFilteredResponseDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.category.Category;
import com.backend.tryal.experience.Experience;
import java.util.*;
import java.util.stream.Collectors;

public class BusinessMapper {
    public static BusinessDTO mapBusinessDTO(Business business) {
        BusinessDTO businessDTO = new BusinessDTO();

        businessDTO.setBusinessId(business.getBusinessId());
        businessDTO.setStripeAccountId(business.getStripeAccountId());
        businessDTO.setName(business.getName());
        businessDTO.setEmail(business.getEmail());
        businessDTO.setWebsite(business.getWebsite());
        businessDTO.setAddress(business.getAddress());
        businessDTO.setPhoneNumber(business.getPhoneNumber());

        // Calculate min and max credits from experiences
        List<Experience> experiences = business.getExperiences(); // Assuming Business has a getExperiences method
        if (experiences != null && !experiences.isEmpty()) {
            List<Integer> creditValues = experiences.stream()
                    .map(Experience::getCreditPrice)
                    .toList();
            businessDTO.setMinCredits(Collections.min(creditValues));
            businessDTO.setMaxCredits(Collections.max(creditValues));
        }

        return businessDTO;
    }

    public static Business mapSignupDTOToBusiness(BusinessSignupDTO signupDTO) {
        Business business = new Business();

        business.setName(signupDTO.getName());
        business.setEmail(signupDTO.getEmail());

        return business;
    } 

    // Private method used with mapFilteredResponse below. Maps categories to experience. Experience DTO is then added to
    // Business filter response
    public static BusinessFilteredResponseDTO mapToBusinessFilteredResponseDTO(Business business, List<Experience> filteredExperiences){
        BusinessFilteredResponseDTO dto = new BusinessFilteredResponseDTO();
        dto.setBusinessId(business.getBusinessId());
        dto.setName(business.getName());
        dto.setAddress(business.getAddress());
        dto.setLatitude(business.getLatitude());
        dto.setLongitude(business.getLongitude());

        // Collect unique category names
        Set<String> categoryNames = filteredExperiences.stream()
                .flatMap(experience -> experience.getCategories().stream())
                .map(Category :: getName)
                .collect(Collectors.toSet());
        dto.setCategories(new ArrayList<>(categoryNames));

        // Collect unique skill levels
        Set<Experience.SkillLevel> skillLevels = filteredExperiences.stream()
                .map(Experience :: getSkillLevel)
                .collect(Collectors.toSet());
        dto.setSkillLevels(new ArrayList<>(skillLevels));

        // Calculate min and max
        List<Integer> creditValues = filteredExperiences.stream()
                .map(Experience::getCreditPrice)
                .toList();
        dto.setMinCredits(Collections.min(creditValues));
        dto.setMaxCredits(Collections.max(creditValues));

        return dto;
    }
}

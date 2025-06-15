package com.backend.tryal.business.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessFilteredResponseDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.category.Category;
import com.backend.tryal.category.dto.FilteredCategoryDTO;
import com.backend.tryal.experience.Experience;

import java.util.ArrayList;
import java.util.List;

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
    private static BusinessFilteredResponseDTO.FilteredExperience getFilteredExperience(Experience e) {
        BusinessFilteredResponseDTO.FilteredExperience expDto = new BusinessFilteredResponseDTO.FilteredExperience();
        expDto.setSkillLevel(e.getSkillLevel());
        expDto.setDuration(e.getDuration());

        // Map Category name and description to experience
        List<FilteredCategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category cat: e.getCategories()) {
            FilteredCategoryDTO catDto = new FilteredCategoryDTO();
            catDto.setName(cat.getName());
            catDto.setDescription(cat.getDescription());
            categoryDTOs.add(catDto);
        }

        expDto.setCategories(categoryDTOs);
        return expDto;
    }

    // Maps the experience to business. Returns the response when filtering for businesses on explore page.
    public static BusinessFilteredResponseDTO mapFilteredResponse(Business business, List<Experience> experiences) {

        BusinessFilteredResponseDTO dto = new BusinessFilteredResponseDTO();
        dto.setBusinessId(business.getBusinessId());
        dto.setName(business.getName());
        dto.setAddress(business.getAddress());
        dto.setLatitude(business.getLatitude());
        dto.setLongitude(business.getLongitude());

        List<BusinessFilteredResponseDTO.FilteredExperience> experienceDTOs = new ArrayList<>();

        for (Experience e: experiences) {
            BusinessFilteredResponseDTO.FilteredExperience expDto = getFilteredExperience(e);
            experienceDTOs.add(expDto);
        }
        dto.setFilteredExperiences(experienceDTOs);
        return dto;
    }
}

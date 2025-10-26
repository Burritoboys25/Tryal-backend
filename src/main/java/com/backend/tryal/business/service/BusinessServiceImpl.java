package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.business.dto.BusinessCreditRangeDTO;
import com.backend.tryal.business.dto.BusinessFilteredRequestDTO;
import com.backend.tryal.business.dto.BusinessFilteredResponseDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.category.Category;
import com.backend.tryal.experience.Experience;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

  private final BusinessRepository businessRepository;

  public BusinessServiceImpl(BusinessRepository businessRepository) {
    this.businessRepository = businessRepository;
  }

  @Override
  public List<Experience> getAllBusinessExperiences(UUID businessId) {
    Business business = getBusinessById(businessId);

    if (business == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }

    return business.getExperiences();
  }

  @Override
  public List<Business> getAllBusinesses() {
    return businessRepository.findAll();
  }

  @Override
  public Business getBusinessById(UUID businessId) {
    Business business = businessRepository.findById(businessId).orElse(null);
    if (business == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }
    return business;
  }

  @Override
  public Business updateBusinessById(UUID businessId, Business business) {
    if (getBusinessById(businessId) == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }

    Business updatedBusiness = getBusinessById(businessId);

    if (business.getStripeAccountId() != null) {
      updatedBusiness.setStripeAccountId(business.getStripeAccountId());
    }

    if (business.getName() != null) {
      updatedBusiness.setName(business.getName());
    }

    if (business.getEmail() != null) {
      updatedBusiness.setEmail(business.getEmail());
    }

    if (business.getPasswordHash() != null) {
      updatedBusiness.setPasswordHash(business.getPasswordHash());
    }

    if (business.getWebsite() != null) {
      updatedBusiness.setWebsite(business.getWebsite());
    }

    if (business.getAddress() != null) {
      updatedBusiness.setAddress(business.getAddress());
    }

    if (business.getPhoneNumber() != null) {
      updatedBusiness.setPhoneNumber(business.getPhoneNumber());
    }

    if (business.getLongitude() != null) {
      updatedBusiness.setLongitude(business.getLongitude());
    }

    if (business.getLatitude() != null) {
      updatedBusiness.setLatitude(business.getLongitude());
    }
    businessRepository.save(updatedBusiness);
    return updatedBusiness;
  }

  @Override
  public void deleteBusinessById(UUID businessId) {
    if (getBusinessById(businessId) == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }
    businessRepository.deleteById(businessId);
  }

  @Override
  public List<BusinessFilteredResponseDTO> getFilteredBusinesses(
      BusinessFilteredRequestDTO filters) {
    Integer limit = filters.getLimit();

    // Normalize filters: treat empty lists as null
    List<Experience.SkillLevel> skillLevel = filters.getSkillLevel();
    if (skillLevel != null && skillLevel.isEmpty()) {
      skillLevel = null; // treat empty as null and return all;
    }

    List<UUID> categoryIds = filters.getCategoryIds();
    if (categoryIds != null && categoryIds.isEmpty()) {
      categoryIds = null; // treat empty as null and return all;;
    }

    List<Business> businesses = businessRepository.findFilteredBusinesses(
        categoryIds,
        filters.getGroupTypeIds(),
        skillLevel,
        filters.getDuration(),
        filters.getCreditsMin(),
        filters.getCreditsMax()
    );

    List<BusinessFilteredResponseDTO> response = new ArrayList<>();

    for (Business business : businesses) {
      List<Experience> filteredExperiences = new ArrayList<>();

      for (Experience experience : business.getExperiences()) {
        boolean matchesSkill =
            skillLevel == null || skillLevel.contains(experience.getSkillLevel());
        boolean matchesDuration =
            filters.getDuration() == null || experience.getDuration() <= filters.getDuration();

        boolean matchesCategory = false;
        if (categoryIds == null) {
          matchesCategory = true; // no filter = allow all
        } else {
          for (Category category : experience.getCategories()) {
            if (categoryIds.contains(category.getCategoryId())) {
              matchesCategory = true;
              break;
            }
          }
        }

        if (matchesSkill && matchesDuration && matchesCategory) {
          filteredExperiences.add(experience);
        }
      }
      // Only include businesses that still have at least one valid experience
      if (!filteredExperiences.isEmpty()) {
        BusinessFilteredResponseDTO dto = BusinessMapper.mapToBusinessFilteredResponseDTO(business,
            filteredExperiences);
        response.add(dto);
      }

    }

    if (limit != null && limit > 0 && response.size() > limit) {
      return response.subList(0, limit);
    }

    return response;
  }

  @Override
  public List<String> getBusinessCategories(UUID businessId) {
    if (getBusinessById(businessId) == null) {
      throw new EntityNotFoundException("Could not find business with id: " + businessId);
    }

    List<Experience> experiences = getAllBusinessExperiences(businessId);
    if (experiences == null || experiences.isEmpty()) {
      return List.of(); // Return an empty list if no experiences are found
    }

    return experiences.stream()
        .flatMap(experience -> experience.getCategories().stream())
        .map(Category::getName)
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  public BusinessCreditRangeDTO getBusinessCreditRangeById(UUID businessId) {
    if (getBusinessById(businessId) != null) {
      return businessRepository.getBusinessCreditRangeById(businessId);
    }
    return null;
  }
}

package com.backend.tryal.business;

import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessFilteredRequestDTO;
import com.backend.tryal.business.dto.BusinessFilteredResponseDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.business.response.BusinessListResponse;
import com.backend.tryal.business.response.BusinessResponse;
import com.backend.tryal.business.service.BusinessService;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.shared.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {
    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    // get all experiences of a business
    @GetMapping("/{businessId}/experiences")
    public List<Experience> getAllBusinessExperiences(@PathVariable UUID businessId) {

      return businessService.getAllBusinessExperiences(businessId);
    }

    // get all Businesses
    @GetMapping()
    public List<BusinessDTO> getAllBusinesses() {
      return businessService.getAllBusinesses()
          .stream()
          .map(BusinessMapper::mapBusinessDTO)
          .collect(Collectors.toList());
    }

    // get Business by ID
    @GetMapping("/{businessId}")
    public BusinessDTO getBusinessById(@PathVariable UUID businessId) {
      Business business = businessService.getBusinessById(businessId);
      return BusinessMapper.mapBusinessDTO(business);
    }

    // Patch Business
    @PatchMapping("/{businessId}")
    public BusinessDTO updateBusinessById(@RequestBody Business business, @PathVariable UUID businessId) {
            Business updatedBusiness = businessService.updateBusinessById(businessId, business);
            return BusinessMapper.mapBusinessDTO(updatedBusiness);
    }

    // Delete Business
    @DeleteMapping("/{businessId}")
    public ApiResponse<String> deleteBusinessById(@PathVariable UUID businessId) {
      businessService.deleteBusinessById(businessId);
      return new ApiResponse<>("success", "Business deleted successfully.");
    }

    @GetMapping("/filter")
    public ResponseEntity<BusinessListResponse> getFilteredBusinesses(
            @RequestParam(required = false) List<UUID> categoryIds,
            @RequestParam(required = false) UUID groupTypeIds,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) List<Experience.SkillLevel> skillLevel,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer creditsMin,
            @RequestParam(required = false) Integer creditsMax
    ) {
        BusinessFilteredRequestDTO filters = new BusinessFilteredRequestDTO();
        filters.setCategoryIds(categoryIds);
        filters.setGroupTypeIds(groupTypeIds);
        filters.setSkillLevel(skillLevel);
        filters.setDuration(duration);
        filters.setCreditsMin(creditsMin);
        filters.setCreditsMax(creditsMax);
        filters.setLimit(limit);

        try {
            List<BusinessFilteredResponseDTO> result = businessService.getFilteredBusinesses(filters);
            if (result.isEmpty()) {
                return new ResponseEntity<>(
                        new BusinessListResponse(result, "No business matched the search criteria. Don't worry it works."),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    new BusinessListResponse(result, "Filtered businesses retrieved successfully."),
                    HttpStatus.OK
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new BusinessListResponse(null, "Invalid input: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new BusinessListResponse(null, "Oppsies an unexpected error occured"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{businessId}/categories")
    public ResponseEntity<List<String>> getBusinessCategories(@PathVariable UUID businessId) {
        try {
            List<String> categories = businessService.getBusinessCategories(businessId);

            if (categories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

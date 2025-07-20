package com.backend.tryal.business;

import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessFilteredRequestDTO;
import com.backend.tryal.business.dto.BusinessFilteredResponseDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.business.response.BusinessListResponse;
import com.backend.tryal.business.response.BusinessResponse;
import com.backend.tryal.business.service.BusinessService;
import com.backend.tryal.experience.Experience;
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
    public ResponseEntity<List<Experience>> getAllBusinessExperiences(@PathVariable UUID businessId) {
        try {

            List<Experience> experiences = businessService.getAllBusinessExperiences(businessId);

            if(experiences == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }else if(experiences.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(experiences, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all Businesses
    @GetMapping()
    public ResponseEntity<List<BusinessDTO>> getAllBusinesses() {
        try {
            List<BusinessDTO> businesses = businessService.getAllBusinesses()
                    .stream()
                    .map(BusinessMapper::mapBusinessDTO)
                    .collect(Collectors.toList());

            if (businesses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(businesses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get Business by ID
    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessResponse> getBusinessById(@PathVariable UUID businessId) {
        try {
            Business business = businessService.getBusinessById(businessId);

            if (business == null) {
                return new ResponseEntity<>(new BusinessResponse(null, "Business not found."),HttpStatus.NOT_FOUND);
            }

            BusinessDTO businessDTO = BusinessMapper.mapBusinessDTO(business);

            return new ResponseEntity<>(new BusinessResponse(businessDTO, "Business found."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Patch Business
    @PatchMapping("/{businessId}")
    public ResponseEntity<BusinessResponse> updateBusinessById(@RequestBody Business business, @PathVariable UUID businessId) {
        try {
            Business updatedBusiness = businessService.updateBusinessById(businessId, business);

            if (updatedBusiness == null) {
                return new ResponseEntity<>(new BusinessResponse(null, "Business not found."), HttpStatus.NOT_FOUND);
            }

            BusinessDTO businessDTO = BusinessMapper.mapBusinessDTO(business);

            return new ResponseEntity<>(new BusinessResponse(businessDTO, "Business updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Business
    @DeleteMapping("/{businessId}")
    public ResponseEntity<String> deleteBusinessById(@PathVariable UUID businessId) {
        try {
            if (businessService.deleteBusinessById(businessId)) {
                return new ResponseEntity<>("Business deleted successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Business not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<BusinessListResponse> getFilteredBusinesses(
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Integer groupTypeIds,
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
}

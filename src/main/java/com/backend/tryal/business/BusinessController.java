package com.backend.tryal.business;

import com.backend.tryal.business.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {
    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    // get all Businesses
    @GetMapping()
    public ResponseEntity<List<Business>> getAllBusinesses() {
        try {
            List<Business> businesses = new ArrayList<Business>(businessService.getAllBusinesses());

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
    public ResponseEntity<Business> getBusinessById(@PathVariable UUID businessId) {
        try {
            Business business = businessService.getBusinessById(businessId);

            if (business == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(business, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create Business
    @PostMapping()
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) {
        try{
            Business newBusiness = businessService.createBusiness(business);
            return new ResponseEntity<>(newBusiness, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Patch Business
    @PatchMapping("/{businessId}")
    public ResponseEntity<Business> updateBusinessById(@RequestBody Business business, @PathVariable UUID businessId) {
        try {
            Business updatedBusiness = businessService.updateBusinessById(businessId, business);

            if (updatedBusiness == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(updatedBusiness, HttpStatus.OK);
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
}

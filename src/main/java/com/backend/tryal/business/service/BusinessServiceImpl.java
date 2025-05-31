package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.experience.Experience;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

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
            return null;
        }

        return business.getExperiences();
    }

    @Override
    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Override
    public Business getBusinessById(UUID businessId) {
        return businessRepository.findById(businessId).orElse(null);
    }

    @Override
    public Business createBusiness(Business business) {
        businessRepository.save(business);

        return business;
    }

    @Override
    public Business updateBusinessById(UUID businessId, Business business) {
        if (getBusinessById(businessId) != null) {
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

            if (business.getWebsite() != null) {
                updatedBusiness.setWebsite(business.getWebsite());
            }

            if (business.getAddress() != null) {
                updatedBusiness.setAddress(business.getAddress());
            }

            if (business.getPhoneNumber() != null) {
                updatedBusiness.setPhoneNumber(business.getPhoneNumber());
            }

            businessRepository.save(updatedBusiness);
            return updatedBusiness;
        }
        return null;
    }

    @Override
    public boolean deleteBusinessById(UUID businessId) {
        if (getBusinessById(businessId) != null) {
            businessRepository.deleteById(businessId);
            return true;
        }

        return false;
    }
}

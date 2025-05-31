package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.experience.Experience;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

public interface BusinessService {
    List<Experience> getAllBusinessExperiences(UUID businessId);
    List<Business> getAllBusinesses();
    Business getBusinessById(UUID businessId);
    Business createBusiness(Business business);
    Business updateBusinessById(UUID businessId, Business business);
    boolean deleteBusinessById(UUID businessId);
}

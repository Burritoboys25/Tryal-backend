package com.backend.h2ak.business.service;

import com.backend.h2ak.business.Business;

import java.util.List;
import java.util.UUID;

public interface BusinessService {
    List<Business> getAllBusinesses();
    Business getBusinessById(UUID businessId);
    Business createBusiness(Business business);
    Business updateBusinessById(UUID businessId, Business business);
    boolean deleteBusinessById(UUID businessId);
}

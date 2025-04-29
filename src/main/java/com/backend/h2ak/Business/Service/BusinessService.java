package com.backend.h2ak.Business.Service;

import com.backend.h2ak.Business.Business;

import java.util.List;
import java.util.UUID;

public interface BusinessService {
    List<Business> getAllBusinesses();
    Business getBusinessById(UUID businessId);
    Business createBusiness(Business business);
    Business updateBusinessById(UUID businessId, Business business);
    boolean deleteBusinessById(UUID businessId);
}

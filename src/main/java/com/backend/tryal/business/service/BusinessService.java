package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.*;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;

import java.util.List;
import java.util.UUID;

public interface BusinessService {
    List<Experience> getAllBusinessExperiences(UUID businessId);
    List<Business> getAllBusinesses();
    Business getBusinessById(UUID businessId);
    Business createBusiness(BusinessSignupDTO signupDTO) throws IllegalArgumentException;
    TokenPair loginBusiness(BusinessLoginDTO loginDTO);
    TokenPair refreshToken(RefreshTokenRequest refreshTokenRequest);
    Business updateBusinessById(UUID businessId, Business business);
    boolean deleteBusinessById(UUID businessId);

    List<BusinessFilteredResponseDTO> getFilteredBusinesses(BusinessFilteredRequestDTO filters);

    BusinessCreditRangeDTO getBusinessCreditRangeById(UUID businessId);
}

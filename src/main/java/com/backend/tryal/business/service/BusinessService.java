package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.*;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;

import java.util.List;
import java.util.UUID;

public interface BusinessService {
    List<Experience> getAllBusinessExperiences(UUID businessId);
    List<Business> getAllBusinesses();
    List<String> getBusinessCategories(UUID businessId);
    Business getBusinessById(UUID businessId);
    Business createBusiness(BusinessSignupDTO signupDTO) throws IllegalArgumentException;
    TokenPairDTO loginBusiness(BusinessLoginDTO loginDTO);
    TokenPairDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
    Business updateBusinessById(UUID businessId, Business business);
    void deleteBusinessById(UUID businessId);

    List<BusinessFilteredResponseDTO> getFilteredBusinesses(BusinessFilteredRequestDTO filters);

    BusinessCreditRangeDTO getBusinessCreditRangeById(UUID businessId);
}

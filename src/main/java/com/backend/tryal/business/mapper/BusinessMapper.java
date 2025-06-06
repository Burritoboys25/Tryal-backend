package com.backend.tryal.business.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.BusinessDTO;

public class BusinessMapper {
    public static BusinessDTO mapBusinessDTO(Business business) {
        BusinessDTO businessDTO = new BusinessDTO();

        businessDTO.setBusinessId(business.getBusinessId());
        businessDTO.setStripeAccountId(business.getStripeAccountId());
        businessDTO.setName(business.getName());
        businessDTO.setEmail(business.getEmail());
        businessDTO.setWebsite(business.getWebsite());
        businessDTO.setAddress(business.getAddress());
        businessDTO.setPhoneNumber(business.getPhoneNumber());
        businessDTO.setExperiences(business.getExperiences());

        return businessDTO;
    }
}

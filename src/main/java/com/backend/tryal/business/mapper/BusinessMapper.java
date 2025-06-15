package com.backend.tryal.business.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;

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

    public static Business mapSignupDTOToBusiness(BusinessSignupDTO signupDTO) {
        Business business = new Business();

        business.setName(signupDTO.getName());
        business.setEmail(signupDTO.getEmail());

        return business;
    }
}

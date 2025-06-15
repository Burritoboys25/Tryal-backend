package com.backend.tryal.business.response;

import com.backend.tryal.business.dto.BusinessDTO;

public class BusinessResponse {
    private BusinessDTO businessDTO;
    private String message;

    public BusinessResponse(BusinessDTO businessDTO, String message) {
        this.businessDTO = businessDTO;
        this.message = message;
    }

    public BusinessDTO getBusinessDTO() {
        return businessDTO;
    }

    public void setBusinessDTO(BusinessDTO businessDTO) {
        this.businessDTO = businessDTO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

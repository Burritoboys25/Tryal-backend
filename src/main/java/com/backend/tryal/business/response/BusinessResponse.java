package com.backend.tryal.business.response;

import com.backend.tryal.business.dto.BusinessDTO;
import lombok.Data;

@Data
public class BusinessResponse {
    private BusinessDTO businessDTO;
    private String message;

    public BusinessResponse(BusinessDTO businessDTO, String message) {
        this.businessDTO = businessDTO;
        this.message = message;
    }
}

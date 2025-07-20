package com.backend.tryal.business.response;

import com.backend.tryal.business.dto.BusinessFilteredResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class BusinessListResponse {
    private List<BusinessFilteredResponseDTO> businesses;
    private String message;

    public BusinessListResponse(List<BusinessFilteredResponseDTO> businesses, String message) {
        this.businesses = businesses;
        this.message = message;
    }
}

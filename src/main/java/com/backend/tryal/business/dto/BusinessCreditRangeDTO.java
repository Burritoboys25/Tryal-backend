package com.backend.tryal.business.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BusinessCreditRangeDTO {
    private UUID businessId;
    private String businessName;
    private Integer minCredit;
    private Integer maxCredit;

    public BusinessCreditRangeDTO(UUID businessId, String businessName, Integer minCredit, Integer maxCredit) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.minCredit = minCredit;
        this.maxCredit = maxCredit;
    }
}

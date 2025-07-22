package com.backend.tryal.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserProfileBookmarkDTO {
    private UUID userId;
    private UUID businessId;
    private String businessName;
    private String address;

    public UserProfileBookmarkDTO(UUID userId, UUID businessId, String businessName, String address) {
        this.userId = userId;
        this.businessId = businessId;
        this.businessName = businessName;
        this.address = address;
    }
}

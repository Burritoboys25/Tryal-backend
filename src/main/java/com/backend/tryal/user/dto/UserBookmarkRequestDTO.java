package com.backend.tryal.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserBookmarkRequestDTO {
    private UUID userId;
    private UUID businessId;

    public UserBookmarkRequestDTO(UUID userId, UUID businessId) {
        this.userId = userId;
        this.businessId = businessId;
    }
}

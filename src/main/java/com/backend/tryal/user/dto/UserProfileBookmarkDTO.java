package com.backend.tryal.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserProfileBookmarkDTO {
    private UUID userId;
    private UUID businessId;
    private String businessName;
    private Integer minCredits;
    private Integer maxCredits;
}

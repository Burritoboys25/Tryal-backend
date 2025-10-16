package com.backend.tryal.security.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    private String refreshToken;

    public RefreshTokenRequestDTO() {}
}

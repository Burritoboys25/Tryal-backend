package com.backend.tryal.security.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenRequest() {}
}

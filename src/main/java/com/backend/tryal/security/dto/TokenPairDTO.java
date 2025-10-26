package com.backend.tryal.security.dto;

import lombok.Data;

@Data
public class TokenPairDTO {
    private String accessToken;
    private String refreshToken;

    public TokenPairDTO() {}

    public TokenPairDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

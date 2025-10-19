package com.backend.tryal.security.service;

import com.backend.tryal.security.dto.TokenPairDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    TokenPairDTO generateTokenPair(Authentication authentication);
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    Boolean validateTokenForUsers(String token, UserDetails userDetails);
    Boolean isValidToken(String token);
    Boolean isRefreshToken(String token);
    Boolean isBusinessUser(String token);
    String extractUsernameFromToken(String token);

}

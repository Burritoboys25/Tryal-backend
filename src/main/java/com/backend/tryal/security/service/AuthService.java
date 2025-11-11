package com.backend.tryal.security.service;

import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.user.dto.UserSignupDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Authentication authenticate(String email, String password);
    AuthenticationResponse registerUser(UserSignupDTO signupDTO);
    AuthenticationResponse registerBusiness(BusinessSignupDTO signupDTO);
    AuthenticationResponse login(LoginRequestDTO loginRequestDTO);
    TokenPairDTO login2(LoginRequestDTO loginRequestDTO);
    TokenPairDTO refresh(String refreshToken);
    void logout();
    void logout2(String accessToken);
    AuthenticationResponse refreshToken(@Valid RefreshTokenRequestDTO refreshTokenRequestDTO);
}

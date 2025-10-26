package com.backend.tryal.security.service;

import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.user.dto.UserSignupDTO;
import jakarta.validation.Valid;

public interface AuthService {
    AuthenticationResponse registerUser(UserSignupDTO signupDTO);
    AuthenticationResponse registerBusiness(BusinessSignupDTO signupDTO);
    AuthenticationResponse login(LoginRequestDTO loginRequestDTO);
    void logout();
    AuthenticationResponse refreshToken(@Valid RefreshTokenRequestDTO refreshTokenRequestDTO);
}

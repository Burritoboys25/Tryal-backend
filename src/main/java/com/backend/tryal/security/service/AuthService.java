package com.backend.tryal.security.service;

import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.user.dto.UserSignupDTO;
import org.springframework.security.core.Authentication;

public interface AuthService {
  Authentication authenticate(String email, String password);
  AuthenticationResponse registerUser(UserSignupDTO signupDTO);
  AuthenticationResponse registerBusiness(BusinessSignupDTO signupDTO);
  TokenPairDTO login(LoginRequestDTO loginRequestDTO);
  TokenPairDTO refresh(String refreshToken);
  void logout(String accessToken);
}

package com.backend.tryal.security.controller;

import com.backend.tryal.business.dto.BusinessLoginDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.service.BusinessService;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.security.service.AuthService;
import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.user.dto.UserLoginDTO;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final BusinessService businessService;
  private final AuthService authService;

  // Signup User
  @PostMapping("/user/signup")
  public AuthenticationResponse signupUser(@Valid @RequestBody UserSignupDTO signupDTO) {
    return authService.registerUser(signupDTO);
  }

  // Signup business
  @PostMapping("/business/signup")
  public AuthenticationResponse signupBusiness(@Valid @RequestBody BusinessSignupDTO signupDTO) {
    return authService.registerBusiness(signupDTO);
  }

  // login user or business
  @PostMapping("/login")
  public AuthenticationResponse login(@Valid @RequestBody LoginRequestDTO loginDTO) {
    return authService.login(loginDTO);
  }

  // logout user/business
  @PostMapping("/logout")
  public ApiResponse<String> logout() {
    authService.logout();
    return new ApiResponse<>("You have successfully logged off");
  }

  // Refresh user/business token
  @PostMapping("/refresh-token")
  public AuthenticationResponse refreshAccessToken(
      @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    return authService.refreshToken(refreshTokenRequestDTO);
  }

  /* DELETE ENDPOINTS BELOW */

  // Login business
  @PostMapping("/business/login")
  public TokenPairDTO loginBusiness(@Valid @RequestBody BusinessLoginDTO loginDTO) {
    return businessService.loginBusiness(loginDTO);
  }

  // Refresh business token
  @PostMapping("/business/refresh-token")
  public TokenPairDTO refreshBusinessToken(
      @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    return businessService.refreshToken(refreshTokenRequestDTO);
  }
}

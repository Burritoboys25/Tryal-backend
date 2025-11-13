package com.backend.tryal.security.controller;

import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.security.service.AuthService;
import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.user.dto.UserSignupDTO;
import jakarta.servlet.http.HttpServletResponse;
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
  public ApiResponse<?> login2(@Valid @RequestBody LoginRequestDTO loginDTO,
      HttpServletResponse response) {
    TokenPairDTO tokenPair = authService.login(loginDTO);

    return new ApiResponse<>(tokenPair);
  }

  // Refresh user/business token
  @PostMapping("/refresh")
  public ApiResponse<?> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    TokenPairDTO tokenPair = authService.refresh(
        refreshTokenRequestDTO.getRefreshToken()); // validates not expired/revoked, returns (userId, newRefresh)

    return new ApiResponse<>(tokenPair);
  }

  // logout user/business
  @PostMapping("/logout")
  public ApiResponse<String> logout(
      @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

    authService.logout(refreshTokenRequestDTO.getRefreshToken());

    return new ApiResponse<>("You have successfully logged off");
  }
}

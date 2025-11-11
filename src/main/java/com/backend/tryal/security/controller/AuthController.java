package com.backend.tryal.security.controller;

import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.security.service.AuthService;
import com.backend.tryal.security.utils.RefreshCookieUtil;
import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.user.dto.UserSignupDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Autowired
  private RefreshCookieUtil refreshCookieUtil;


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

  // login user or business
  @PostMapping("/login2")
  public ApiResponse<String> login2(@Valid @RequestBody LoginRequestDTO loginDTO, HttpServletResponse response) {
    TokenPairDTO tokenPair = authService.login2(loginDTO);

    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookieUtil.refreshCookie(
        tokenPair.getRefreshToken(), Duration.ofDays(30), true).toString() );

    return new ApiResponse<>(tokenPair.getAccessToken());
  }

  // Refresh user/business token
  @PostMapping("/refresh")
  public ApiResponse<String> refresh(@CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
    TokenPairDTO tokenPair = authService.refresh(refreshToken); // validates not expired/revoked, returns (userId, newRefresh)

    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookieUtil.refreshCookie(
        tokenPair.getRefreshToken(), Duration.ofDays(30), true).toString() );

    return  new ApiResponse<>(tokenPair.getAccessToken());
  }

  // logout user/business
  @PostMapping("/logout")
  public ApiResponse<String> logout(HttpServletRequest request) {
    authService.logout();

    return new ApiResponse<>("You have successfully logged off");
  }

  // logout user/business
  @PostMapping("/logout2")
  public ApiResponse<String> logout2(HttpServletRequest request, HttpServletResponse response) {
    ResponseCookie expiredCookie = refreshCookieUtil.refreshCookie(
        "",
        Duration.ZERO,
        true
    );
    response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());

    String authHeader = request.getHeader("Authorization");
    String token = authHeader.substring(7);
    authService.logout2(token);

    return new ApiResponse<>("You have successfully logged off");
  }

  // Refresh user/business token
  @PostMapping("/refresh-token")
  public AuthenticationResponse refreshAccessToken(
      @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    return authService.refreshToken(refreshTokenRequestDTO);
  }
}

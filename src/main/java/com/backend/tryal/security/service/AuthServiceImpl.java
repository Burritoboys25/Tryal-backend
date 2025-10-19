package com.backend.tryal.security.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.repository.TokenRepository;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class AuthServiceImpl implements AuthService{

  private final UserRepository userRepository;
  private final BusinessRepository businessRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final TokenRepository tokenRepository;

  private final UserDetailsService userDetailsService;
  private final UserDetailsService businessDetailsService;

  public AuthServiceImpl(UserRepository userRepository, BusinessRepository businessRepository,
                         AuthenticationManager authenticationManager,
                         JwtService jwtService, TokenRepository tokenRepository,
                         @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                         @Qualifier("customBusinessDetailsService") UserDetailsService businessDetailsService) {
    this.userRepository = userRepository;
    this.businessRepository = businessRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.tokenRepository = tokenRepository;
    this.userDetailsService = userDetailsService;
    this.businessDetailsService = businessDetailsService;
  }

  // Register a new User
  public AuthenticationResponse registerUser(UserSignupDTO signupDTO) {
    if (userRepository.existsByEmail(signupDTO.getEmail()) || businessRepository.existsByEmail(
        signupDTO.getEmail())) {
      throw new IllegalArgumentException("Email is already taken.");
    }

    User user = UserMapper.mapSignupDTOToUser(signupDTO);
    String encodedPassword = this.passwordEncoder.encode(signupDTO.getPassword());
    user.setPasswordHash(encodedPassword);
    userRepository.save(user);

    return authenticateUser(signupDTO.getEmail(), signupDTO.getPassword());
  }

  // Register a new business
  public AuthenticationResponse registerBusiness(BusinessSignupDTO signupDTO) {
    if (businessRepository.existsByEmail(signupDTO.getEmail()) || userRepository.existsByEmail(
        signupDTO.getEmail())) {
      throw new IllegalArgumentException("Email is already taken.");
    }

    Business business = BusinessMapper.mapSignupDTOToBusiness(signupDTO);
    String encodedPassword = this.passwordEncoder.encode(signupDTO.getPassword());
    business.setPasswordHash(encodedPassword);
    businessRepository.save(business);

    return authenticateUser(signupDTO.getEmail(), signupDTO.getPassword());
  }

  // Login user/business
  public AuthenticationResponse login(LoginRequestDTO loginRequestDTO) {
    return authenticateUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
  }

  private AuthenticationResponse authenticateUser(String email, String password) {
    // Authenticate user
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));

    // Set authentication in security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Generate Token Pair
    TokenPairDTO tokenPairDTO = jwtService.generateTokenPair(authentication);

    // Store token in Redis
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    tokenRepository.storeTokens(
        userDetails.getUsername(),
        tokenPairDTO.getAccessToken(),
        tokenPairDTO.getRefreshToken()
    );

    return new AuthenticationResponse(
        tokenPairDTO.getAccessToken(),
        tokenPairDTO.getRefreshToken(),
        userDetails.getUsername()
    );
  }

  // logout user/business
  public void logout() {
    // Get current authenticated user
    UserDetails userDetails = (UserDetails) SecurityContextHolder
        .getContext().getAuthentication().getPrincipal();

    // remove all tokens for this user
    tokenRepository.removeAllTokens(userDetails.getUsername());
  }

  // refresh token
  public AuthenticationResponse refreshToken(@Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
    String refreshToken = refreshTokenRequestDTO.getRefreshToken();

    // check if still valid refresh token
    if (!jwtService.isRefreshToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    // check if token is blacklisted
    if (tokenRepository.isRefreshTokenBlackListed(refreshToken)) {
      throw new IllegalArgumentException("Refresh token is blacklisted");
    }

    String email = jwtService.extractUsernameFromToken(refreshToken);

    // verify token matches stored token for user
    String storedRefreshToken = tokenRepository.getRefreshToken(email);

    if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
      throw HttpClientErrorException.Unauthorized.create(HttpStatus.UNAUTHORIZED, "Unauthorized",
          null,
          "Invalid refresh token".getBytes(), null);
    }

    boolean isBusiness = jwtService.isBusinessUser(refreshToken);
    UserDetails userDetails;

    if (isBusiness) {
      userDetails = businessDetailsService.loadUserByUsername(email);
    } else {
      userDetails = userDetailsService.loadUserByUsername(email);
    }

    // Create authentication object
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

    // Generate new access token
    String newAccessToken = jwtService.generateAccessToken(authenticationToken);

    // Update access token in redis
    tokenRepository.removeAccessToken(email);

    tokenRepository.storeTokens(
        userDetails.getUsername(),
        newAccessToken,
        refreshToken
    );

    return new AuthenticationResponse(
        newAccessToken,
        refreshToken,
        userDetails.getUsername()
    );
  }
}

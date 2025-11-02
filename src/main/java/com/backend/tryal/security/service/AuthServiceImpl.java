package com.backend.tryal.security.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.model.BusinessPrincipal;
import com.backend.tryal.security.model.UserPrincipal;
import com.backend.tryal.security.repository.TokenRepository;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.mapper.UserMapper;
import jakarta.validation.Valid;
import java.util.UUID;
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
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

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

    // 5) Load principal (user or business) based on claim
    final boolean isBusiness = jwtService.isBusinessUser(tokenPairDTO.getRefreshToken());
    UUID id = isBusiness ? ((BusinessPrincipal) userDetails).getBusinessId()
        : ((UserPrincipal) userDetails).getUserId();

    return new AuthenticationResponse(
        tokenPairDTO.getAccessToken(),
        tokenPairDTO.getRefreshToken(),
        id
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
  public AuthenticationResponse refreshToken(@Valid RefreshTokenRequestDTO dto) {
    final String refreshToken = dto.getRefreshToken();

    try {
      // 1) Must be a valid *refresh* token (structure/signature/claims)
      if (!jwtService.isRefreshToken(refreshToken)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
      }

      // 2) Not blacklisted
      if (tokenRepository.isRefreshTokenBlackListed(refreshToken)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is blacklisted");
      }

      // 3) Extract subject
      final String email = jwtService.extractUsernameFromToken(refreshToken);

      // 4) Must match the stored refresh token for this user
      final String storedRefreshToken = tokenRepository.getRefreshToken(email);
      if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
      }

      // 5) Load principal (user or business) based on claim
      final boolean isBusiness = jwtService.isBusinessUser(refreshToken);
      final UserDetails userDetails = isBusiness
          ? businessDetailsService.loadUserByUsername(email)
          : userDetailsService.loadUserByUsername(email);

      UUID id = isBusiness ? ((BusinessPrincipal) userDetails).getBusinessId()
          : ((UserPrincipal) userDetails).getUserId();

      // 6) Create auth and mint new access token
      final UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

      final String newAccessToken = jwtService.generateAccessToken(authenticationToken);

      // 7) Rotate access token in Redis (keep same refresh token)
      tokenRepository.removeAccessToken(email);
      tokenRepository.storeTokens(userDetails.getUsername(), newAccessToken, refreshToken);

      // 8) Return response
      return new AuthenticationResponse(newAccessToken, refreshToken,
          id);

    } catch (io.jsonwebtoken.JwtException ex) {
      // Signature/expired/malformed tokens → 401
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", ex);
    }
  }
}

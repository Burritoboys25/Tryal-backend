package com.backend.tryal.security.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.security.dto.LoginRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.model.BusinessPrincipal;
import com.backend.tryal.security.model.UserPrincipal;
import com.backend.tryal.security.repository.TokenRepository;
import com.backend.tryal.security.response.AuthenticationResponse;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.mapper.UserMapper;
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
      @Qualifier("customBusinessDetailsService") UserDetailsService businessDetailsService
      ) {
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
  public TokenPairDTO login(LoginRequestDTO loginRequestDTO) {
    Authentication auth = authenticate(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    String access = jwtService.generateAccessToken(auth);
    String refresh = jwtService.issueAndStoreRefreshToken(auth);

    return new TokenPairDTO(access, refresh);
  }

  // refresh and rotate token
  public TokenPairDTO refresh(String refreshToken) {
    // 1) Must be a valid *refresh* token (structure/signature/claims)
    if (!jwtService.isRefreshToken(refreshToken)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

    // 2) Not blacklisted
    if (tokenRepository.isRefreshTokenBlackListed(refreshToken)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is blacklisted");
    }

    // Check if token is expired
    if (jwtService.isTokenExpired(refreshToken)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid: Token is expired");
    }

    // 3) Extract username
    final String email = jwtService.extractUsernameFromToken(refreshToken);
    final UUID id = UUID.fromString(jwtService.extractSubjectFromToken(refreshToken));

    // 5) Load principal (user or business) based on claim
    final boolean isBusiness = jwtService.isBusinessUser(refreshToken);
    final UserDetails userDetails = isBusiness
        ? businessDetailsService.loadUserByUsername(email)
        : userDetailsService.loadUserByUsername(email);

    // 6) Create auth and mint new access token
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

    final String newAccessToken = jwtService.generateAccessToken(authenticationToken);

    // 7) Rotate refresh token -- blacklist and issue new one
    tokenRepository.removeRefreshToken(id);
    final String newRefreshToken = jwtService.issueAndStoreRefreshToken(authenticationToken);

    return new TokenPairDTO(newAccessToken, newRefreshToken);
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
    // 5) Load principal (user or business) based on claim
    final boolean isBusiness = jwtService.isBusinessUser(tokenPairDTO.getRefreshToken());
    UUID id = isBusiness ? ((BusinessPrincipal) userDetails).getBusinessId()
        : ((UserPrincipal) userDetails).getUserId();

    tokenRepository.storeTokens(
        id,
        tokenPairDTO.getAccessToken(),
        tokenPairDTO.getRefreshToken()
    );

    return new AuthenticationResponse(
        tokenPairDTO.getAccessToken(),
        tokenPairDTO.getRefreshToken(),
        id
    );
  }

  // authenticate user
  public Authentication authenticate(String email, String password) {
    // Authenticate user
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));

    // Set authentication in security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return authentication;
  }

  // logout user/business
  public void logout(String refreshToken) {
    final UUID id = UUID.fromString(jwtService.extractSubjectFromToken(refreshToken));
    tokenRepository.removeRefreshToken(id);
  }
}

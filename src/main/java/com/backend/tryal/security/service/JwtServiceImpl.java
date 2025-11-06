package com.backend.tryal.security.service;

import com.backend.tryal.security.dto.TokenPairDTO;
import com.backend.tryal.security.model.BusinessPrincipal;
import com.backend.tryal.security.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService{

  private String jwtSecret = "";

  @Value("${jwt.issuer}")
  private String issuer;

  @Value("${app.jwt.expiration}")
  private long jwtExpiration;

  @Value("${app.jwt.refresh-expiration}")
  private long refreshExpiration;

  public JwtServiceImpl() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      SecretKey sk = keyGenerator.generateKey();
      jwtSecret = Base64.getEncoder().encodeToString(sk.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public TokenPairDTO generateTokenPair(Authentication authentication) {
    String accessToken = generateAccessToken(authentication);
    String refreshToken = generateRefreshToken(authentication);

    return new TokenPairDTO(accessToken, refreshToken);
  }

  // Generate access token
  public String generateAccessToken(Authentication authentication) {
    return generateToken(authentication, jwtExpiration, new HashMap<>());
  }

  // Generate refresh token
  public String generateRefreshToken(Authentication authentication) {
    Map<String, String> claims = new HashMap<>();
    claims.put("tokenType", "refresh");

    return generateToken(authentication, refreshExpiration, claims);
  }

  private String generateToken(Authentication authentication, long expirationTime,
      Map<String, String> claims) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

    boolean isBusiness = userPrincipal.getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals("BUSINESS"));

    Date now = new Date(); // Time of token creation
    Date expiryDate = new Date(
        now.getTime() + expirationTime); // Time of token expiration -- set to 60 mins

    // Add accountType to the claims
    claims.put("accountType", isBusiness ? "BUSINESS" : "USER");

    claims.put("username", userPrincipal.getUsername());

    UUID subId;
    if (isBusiness) {
      subId = ((BusinessPrincipal) userPrincipal).getBusinessId();
    } else {
      subId = ((UserPrincipal) userPrincipal).getUserId();
    }

    return Jwts.builder()
        .header()
        .add("typ", "JWT")
        .and()
        .subject(subId.toString())
        .claims(claims)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSignInKey())
        .compact();
  }

  // Validate token
  public Boolean validateTokenForUsers(String token, UserDetails userDetails) {
    final String username = extractUsernameFromToken(token);// Extract email from token

    return username != null && username.equals(userDetails.getUsername());
  }

  public Boolean isValidToken(String token) {
    return extractAllClaims(token) != null;
  }

  // Validate if refresh token
  public Boolean isRefreshToken(String token) {
    Claims claims = extractAllClaims(token);

    if (claims == null) {
      return false;
    }

    return "refresh".equals(claims.get("tokenType"));
  }

  public Boolean isBusinessUser(String token) {
    Claims claims = extractAllClaims(token);

    if (claims == null) {
      return false;
    }

    //return jwtService.extract(jwt).containsKey("isBusiness");
    return "BUSINESS".equals(claims.get("accountType"));
  }

  private Claims extractAllClaims(String token) {
    Claims claims = null;
    try {
      claims = Jwts.parser()
          .verifyWith(getSignInKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    return claims;
  }

  public String extractUsernameFromToken(String token) {
    Claims claims = extractAllClaims(token);

    if (claims != null) {
      return claims.get("username").toString();
    }
    return null;
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}

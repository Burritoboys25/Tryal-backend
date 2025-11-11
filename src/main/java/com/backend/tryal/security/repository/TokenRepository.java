package com.backend.tryal.security.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository {
  private final RedisTemplate<String, Object> redisTemplate;

  // key prefixes for token storage
  private static final String ACCESS_TOKEN_KEY_PREFIX = "user:access:";
  private static final String REFRESH_TOKEN_KEY_PREFIX = "user:refresh:";

  // key prefixes for token blacklisting
  private static final String ACCESS_BLACKLIST_PREFIX = "blacklist:access:";
  private static final String REFRESH_BLACKLIST_PREFIX = "blacklist:refresh:";

  @Value("${app.jwt.expiration}")
  private long jwtExpiration;

  @Value("${app.jwt.refresh-expiration}")
  private long refreshTokenExpiration;

  public TokenRepository(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  //  Store both access and refresh token for user
  public void storeTokens(
      UUID id,
      String accessToken,
      String refreshToken
  ) {
//    Store access token
    String accessKey = ACCESS_TOKEN_KEY_PREFIX+ id;
    storeToken(accessKey, accessToken, jwtExpiration);

//    store refresh token
    String refreshKey = REFRESH_TOKEN_KEY_PREFIX+ id;
    storeToken(refreshKey, refreshToken, refreshTokenExpiration);
  }

  private void storeToken(String key, String token, long expiration) {
    redisTemplate.opsForValue().set(key, token);
    redisTemplate.expire(key, expiration, TimeUnit.MILLISECONDS);
  }

  // Store refresh token
  public void storeRefreshToken(UUID id, String refreshToken) {
    String refreshKey = REFRESH_TOKEN_KEY_PREFIX+id;
    storeToken(refreshKey, refreshToken, refreshTokenExpiration);
  }

  // retrieve access token for a user
  public String getAccessToken(UUID id) {
    String accessKey = ACCESS_TOKEN_KEY_PREFIX + id;
    return getToken(accessKey);
  }

  // retrieve refresh token for a user
  public String getRefreshToken(UUID id) {
    String refreshKey = REFRESH_TOKEN_KEY_PREFIX + id;
    return getToken(refreshKey);
  }

  private String getToken(String accessKey) {
    Object token = redisTemplate.opsForValue().get(accessKey);
    return token != null ? token.toString() : null;
  }

  // remove all tokens for a user -- (COMPLETE LOGOUT)
  public void removeAllTokens(UUID id) {
    String accessToken = getAccessToken(id);
    String refreshToken = getRefreshToken(id);

    // Remove tokens
    String accessKey = ACCESS_TOKEN_KEY_PREFIX + id;
    String refreshKey = REFRESH_TOKEN_KEY_PREFIX + id;
    redisTemplate.delete(accessKey);
    redisTemplate.delete(refreshKey);

    // Blacklist token
    if (accessToken != null) {
      String accessBlackListKey = ACCESS_BLACKLIST_PREFIX+accessToken;
      blackListToken(accessBlackListKey, jwtExpiration);
    }

    if (refreshToken != null) {
      String refreshBlackListKey = REFRESH_BLACKLIST_PREFIX+refreshToken;
      blackListToken(refreshBlackListKey, refreshTokenExpiration);
    }
  }

  public void removeAccessToken(UUID id) {
    String accessToken = getAccessToken(id);
    String accessKey = ACCESS_TOKEN_KEY_PREFIX + id;
    redisTemplate.delete(accessKey);

    // Blacklist token
    String accessBlackListKey = ACCESS_BLACKLIST_PREFIX+accessToken;
    blackListToken(accessBlackListKey, jwtExpiration);
  }

  public void removeRefreshToken(UUID id) {
    String refreshToken = getRefreshToken(id);
    String refreshKey = REFRESH_TOKEN_KEY_PREFIX + id;
    redisTemplate.delete(refreshKey);

    // Blacklist token
    String refreshBlackListKey = REFRESH_BLACKLIST_PREFIX+refreshToken;
    blackListToken(refreshBlackListKey, refreshTokenExpiration);
  }

  private void blackListToken(String blacklistKey, long expiration) {
    redisTemplate.opsForValue().set(blacklistKey, "blacklisted");
    redisTemplate.expire(blacklistKey, expiration, TimeUnit.MILLISECONDS);
  }

  public boolean isAccessTokenBlackListed(String token) {
    String key = ACCESS_BLACKLIST_PREFIX + token;
    return redisTemplate.hasKey(key);
  }

  public boolean isRefreshTokenBlackListed(String refreshToken) {
    String key = REFRESH_BLACKLIST_PREFIX + refreshToken;
    return redisTemplate.hasKey(key);
  }

}

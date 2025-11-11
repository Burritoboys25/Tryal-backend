package com.backend.tryal.security.utils;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshCookieUtil {
  @Value("${spring.application.environment}")
  private String envVariable;

  private RefreshCookieUtil() {};

  public ResponseCookie refreshCookie(String token, Duration maxAge, boolean crossSite) {
    boolean cookieSecure = !envVariable.equals("dev");

    ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from("refresh_token", token)
        .httpOnly(true)
        .secure(cookieSecure) // True = Only send this cookie over HTTPS connections
        .path("/")
        .maxAge(maxAge);
    if (crossSite) b.sameSite("None"); else b.sameSite("Lax");
    return b.build();
  }
}

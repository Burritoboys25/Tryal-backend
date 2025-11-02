package com.backend.tryal.security.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
  private String accessToken;
  private String refreshToken;
  private UUID id;
}

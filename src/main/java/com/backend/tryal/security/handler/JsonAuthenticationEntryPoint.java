package com.backend.tryal.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException ex) throws IOException {

    log.warn("JsonAuthenticationEntryPoint invoked for {} (reason: {})",
        request.getRequestURI(), ex.getClass().getSimpleName());

    String body = """
        {
          "timestamp":"%s",
          "status":401,
          "error":"Unauthorized",
          "message":"Invalid or missing token",
          "path":"%s"
        }
        """.formatted(
        OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        request.getRequestURI()
    ).trim();

    // ensure nothing already written remains
    response.resetBuffer();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(body);
    response.getWriter().flush();
    // do NOT call chain here; ExceptionTranslationFilter stops the flow
  }
}
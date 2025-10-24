package com.backend.tryal.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException ex) throws IOException {

    log.warn("JsonAccessDeniedHandler invoked for {} (reason: {})",
        request.getRequestURI(), ex.getClass().getSimpleName());

    String body = """
        {
          "timestamp":"%s",
          "status":403,
          "error":"Forbidden",
          "message":"Access denied",
          "path":"%s"
        }
        """.formatted(
        OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        request.getRequestURI()
    ).trim();

    response.resetBuffer();
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(body);
    response.getWriter().flush();
  }
}
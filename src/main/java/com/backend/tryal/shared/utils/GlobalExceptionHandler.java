package com.backend.tryal.shared.utils;

import com.backend.tryal.shared.response.ErrorResponse;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse<String>> handleNotFoundException(EntityNotFoundException e,
      HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.NOT_FOUND,
        e.getMessage(),
        request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse<String>> handleIllegalArgumentException(
      IllegalArgumentException e, HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.BAD_REQUEST,
        e.getMessage(),
        request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse<String>> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e, HttpServletRequest request) {
    String message = ExceptionUtil.getHttpRequestNotReadableMessage(e);

    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.BAD_REQUEST, message,
        request.getRequestURI(), e);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse<String>> handleTypeMismatch(
      MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    String message = ExceptionUtil.getTypeMismatchMessage(e.getName(), String.valueOf(e.getValue()),
        e.getRequiredType());
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.BAD_REQUEST, message,
        request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(StripeException.class)
  public ResponseEntity<ErrorResponse<String>> handleStripeException(StripeException e,
      HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.BAD_GATEWAY,
        e.getMessage(),
        request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
  }

  @ExceptionHandler(com.stripe.exception.SignatureVerificationException.class)
  public ResponseEntity<ErrorResponse<String>> handleSignatureVerificationException(
      com.stripe.exception.SignatureVerificationException e,
      HttpServletRequest request) {

    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(
        HttpStatus.UNAUTHORIZED,
        "Invalid Stripe webhook signature: " + e.getMessage(),
        request.getRequestURI(),
        e
    );

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorResponse<String>> handleIOException(IOException e,
      HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        e.getMessage(),
        request.getRequestURI(),
        e
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse<String>> handleIllegalStateException(
      IllegalStateException e, HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(
        HttpStatus.UNPROCESSABLE_ENTITY,
        e.getMessage(),
        request.getRequestURI(),
        e
    );
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
  }

  @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
  public ResponseEntity<ErrorResponse<String>> handleUnauthorizedException(
      HttpClientErrorException.Unauthorized e,
      HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.UNAUTHORIZED,
        e.getMessage(), request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler({org.springframework.security.core.AuthenticationException.class})
  public ResponseEntity<ErrorResponse<String>> handleAuthenticationException(
      org.springframework.security.core.AuthenticationException e,
      HttpServletRequest request) {

    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(
        HttpStatus.UNAUTHORIZED,
        "Invalid email or password", // avoid leaking details
        request.getRequestURI(),
        e
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(io.jsonwebtoken.JwtException.class)
  public ResponseEntity<ErrorResponse<String>> handleJwtException(
      io.jsonwebtoken.JwtException e, HttpServletRequest request) {

    ErrorResponse<String> body = ExceptionUtil.buildErrorResponse(
        HttpStatus.UNAUTHORIZED,
        "Invalid refresh token",
        request.getRequestURI(),
        e
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  // 1) Respect ResponseStatusException's own status & reason
  @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
  public ResponseEntity<ErrorResponse<String>> handleRSE(
      org.springframework.web.server.ResponseStatusException e,
      jakarta.servlet.http.HttpServletRequest request) {

    var status = e.getStatusCode(); // HttpStatusCode in Boot 3
    ErrorResponse<String> body = ExceptionUtil.buildErrorResponse(
        org.springframework.http.HttpStatus.valueOf(status.value()),
        e.getReason() != null ? e.getReason() : "Request failed",
        request.getRequestURI(),
        e
    );
    return ResponseEntity.status(status).body(body);
  }

  // 4) Forbidden (authenticated but insufficient authority) → 403
  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public ResponseEntity<ErrorResponse<String>> handleDenied(
      org.springframework.security.access.AccessDeniedException e,
      jakarta.servlet.http.HttpServletRequest request) {

    ErrorResponse<String> body = ExceptionUtil.buildErrorResponse(
        org.springframework.http.HttpStatus.FORBIDDEN,
        "Access denied",
        request.getRequestURI(),
        e
    );
    return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse<String>> handleGeneralException(Exception e,
      HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        e.getMessage(), request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}


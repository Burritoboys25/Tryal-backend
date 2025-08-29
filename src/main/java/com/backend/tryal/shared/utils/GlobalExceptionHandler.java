package com.backend.tryal.shared.utils;

import com.backend.tryal.shared.response.ErrorResponse;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse<String>> handleGeneralException(Exception e,
      HttpServletRequest request) {
    ErrorResponse<String> error = ExceptionUtil.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        e.getMessage(), request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}


package com.backend.tryal.shared.utils;

import com.backend.tryal.shared.response.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.backend.tryal.shared.utils.ExceptionUtil.buildErrorResponse;
import static com.backend.tryal.shared.utils.ExceptionUtil.getTypeMismatchMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        ErrorResponse<String> error = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                e
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse<String>> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ErrorResponse<String> error = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                e
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        String message = "Malformed JSON request body";

        Throwable cause = e.getCause();

        if (cause instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
            message = ExceptionUtil.getTypeMismatchMessage(fieldName, String.valueOf(ife.getValue()), ife.getTargetType()
            );
        }
        else if (cause instanceof JsonMappingException jme) {
            String fieldName = jme.getPath().isEmpty() ? "unknown" : jme.getPath().get(0).getFieldName();
            message = String.format("Invalid format for field '%s': %s", fieldName, jme.getOriginalMessage());
        }

        ErrorResponse<String> error = buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String message = getTypeMismatchMessage(e.getName(), String.valueOf(e.getValue()), e.getRequiredType());
        ErrorResponse<String> error = buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse<String>> handleStripeException(StripeException e, HttpServletRequest request) {
        ErrorResponse<String> error = buildErrorResponse(HttpStatus.BAD_GATEWAY, e.getMessage(), request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<String>> handleGeneralException(Exception e, HttpServletRequest request) {
        ErrorResponse<String> error = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}


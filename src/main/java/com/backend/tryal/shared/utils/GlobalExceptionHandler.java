package com.backend.tryal.shared.utils;

import com.backend.tryal.shared.response.ApiResponse;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(Exception e) {
        ApiResponse<String> errorResponse = new ApiResponse<>(
                "error",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<String> handleStripeException(StripeException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        ApiResponse<String> errorResponse = new ApiResponse<>(
                "error",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


package com.backend.tryal.shared.utils;
import com.backend.tryal.shared.response.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class ExceptionUtil {
    private ExceptionUtil() {};

    // Helper to reduce repetition
    public static ErrorResponse<String> buildErrorResponse(HttpStatus status, String message, String path, Exception e) {
        return new ErrorResponse<>(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                ExceptionUtil.getStackTraceAsString(e)
        );
    }

    public static void validateUUIDOrThrow(UUID uuid) {
        try {
            UUID.fromString(String.valueOf(uuid));
        } catch (IllegalArgumentException e) {
            // Will get caught by your @ExceptionHandler
            throw new IllegalArgumentException("Invalid UUID: " + uuid);
        }
    }

    public static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) return "";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static String getHttpRequestNotReadableMessage(HttpMessageNotReadableException e) {
      String message = "Malformed JSON request body";

      Throwable cause = e.getCause();

      if (cause instanceof InvalidFormatException ife) {
        String fieldName = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
        message = ExceptionUtil.getTypeMismatchMessage(
            fieldName,
            String.valueOf(ife.getValue()),
            ife.getTargetType()
        );
      } else if (cause instanceof JsonMappingException jme) {
        String fieldName = jme.getPath().isEmpty() ? "unknown" : jme.getPath().get(0).getFieldName();
        message = String.format("Invalid format for field '%s': %s", fieldName,
            jme.getOriginalMessage());
      }
      return message;
    }

    public static String getTypeMismatchMessage(String paramName, String rejectedValue, Class<?> requiredType) {
        if (requiredType == null) {
            return String.format("Invalid value '%s' for parameter '%s'", rejectedValue, paramName);
        }
        if (requiredType == UUID.class) {
            return String.format("Parameter '%s' must be a valid UUID, but got '%s'", paramName, rejectedValue);
        }
        if (Number.class.isAssignableFrom(requiredType) || requiredType.isPrimitive()) {
            return String.format("Parameter '%s' must be a number of type %s, but got '%s'",
                    paramName, requiredType.getSimpleName(), rejectedValue);
        }
        if (requiredType == Boolean.class || requiredType == boolean.class) {
            return String.format("Parameter '%s' must be 'true' or 'false', but got '%s'",
                    paramName, rejectedValue);
        }
        if (requiredType.isEnum()) {
            String validValues = Arrays.stream(requiredType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            return String.format("Parameter '%s' must be one of [%s], but got '%s'",
                    paramName, validValues, rejectedValue);
        }
        return String.format("Parameter '%s' with value '%s' could not be converted to type %s",
                paramName, rejectedValue, requiredType.getSimpleName());
    }

}

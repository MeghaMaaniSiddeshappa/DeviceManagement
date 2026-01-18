package com.dm.devicesapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - Bean Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException( MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getBindingResult().getAllErrors());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                (error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // 404 - Device Not Found
    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDeviceDoesNotExistsException( DeviceNotFoundException ex) {
        log.warn("Device not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    // 409 - Device In Use
    @ExceptionHandler(DeviceInUseException.class)
    public ResponseEntity<Map<String, String>> handleDeviceIsInUseException( DeviceInUseException ex) {
        log.warn("Device in use: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }

    // 400 - Enum Parsing Errors in Request Body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumParse(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON or enum parsing error: {}", ex.getMessage());

        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof InvalidFormatException e &&
                    e.getTargetType().isEnum()) {

                Map<String, String> errors = new HashMap<>();
                errors.put("state", "Invalid state. Allowed values: AVAILABLE, IN_USE, INACTIVE");
                return ResponseEntity.badRequest().body(errors);
            }
            cause = cause.getCause();
        }

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Malformed JSON request");
        return ResponseEntity.badRequest().body(errors);
    }

    // 400 - Enum Parsing Errors in Query Parameters
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleEnumQueryParamError(
            MethodArgumentTypeMismatchException ex) {
        log.warn("Query parameter type mismatch: {}", ex.getMessage());

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {

            Map<String, String> error = new HashMap<>();
            error.put(ex.getName(),
                    "Invalid value. Allowed values: AVAILABLE, IN_USE, INACTIVE" );
            return ResponseEntity.badRequest().body(error);
        }

        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid request parameter");
        return ResponseEntity.badRequest().body(error);
    }
}

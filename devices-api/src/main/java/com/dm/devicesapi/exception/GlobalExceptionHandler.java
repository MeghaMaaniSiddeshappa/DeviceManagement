package com.dm.devicesapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException( MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                (error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDeviceDoesNotExistsException( DeviceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DeviceInUseException.class)
    public ResponseEntity<Map<String, String>> handleDeviceIsInUseException( DeviceInUseException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumParse(HttpMessageNotReadableException ex) {

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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleEnumQueryParamError(
            MethodArgumentTypeMismatchException ex) {

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

package com.secure_auth.authdemo.exception;

import com.secure_auth.authdemo.exception.dto.ApiErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiErrorResponse response = new ApiErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Validation Failed");
        response.setMessage("One or more fields are invalid");
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredsError(BadCredentialsException e) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError("Unauthorized");
        response.setMessage("Invalid username or password");
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(null); // Or new HashMap<>() if needed

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwtToken(JwtException e) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError("Session timed out");
        error.setMessage("Session timed out");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgs(IllegalArgumentException ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse();
        Map<String, String> mp = new HashMap<>();
        mp.put(ex.getMessage(), ex.getLocalizedMessage());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setError("Conflict");
        error.setMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setErrors(mp);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralErrors(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError("Internal Server Error");
        response.setMessage("Something went wrong. Please try again later.");
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

package com.example.qzone.dto;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice public class ApiErrorHandler { @ExceptionHandler(ApiException.class) ResponseEntity<Map<String, String>> handle(ApiException e) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage())); } }

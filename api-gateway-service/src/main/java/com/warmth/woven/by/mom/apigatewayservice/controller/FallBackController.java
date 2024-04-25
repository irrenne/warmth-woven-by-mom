package com.warmth.woven.by.mom.apigatewayservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

  private static final String FALLBACK_MESSAGE = "Service is currently unavailable. Please try again later.";

  @GetMapping("/productServiceFallback")
  public ResponseEntity<String> productServiceFallback() {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FALLBACK_MESSAGE);
  }

  @PostMapping("/orderServiceFallback")
  public ResponseEntity<String> orderServiceFallback() {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FALLBACK_MESSAGE);
  }

  @PostMapping("/userServiceFallback")
  public ResponseEntity<String> userServiceFallback() {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FALLBACK_MESSAGE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FALLBACK_MESSAGE);
  }
}


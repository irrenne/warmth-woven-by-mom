package com.warmth.woven.by.mom.apigatewayservice.controller;

import jakarta.ws.rs.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FallBackController {

  private static final String FALLBACK_MESSAGE = "Сервіс тимчасово недоступний. Будь ласка, спробуйте пізніше.";

  private static final String UNAUTHORIZED_MESSAGE = "Невірно введені дані, спробуйте ще раз";

  private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Упс, щось пішло не так. Будь ласка, спробуйте пізніше.";

  @RequestMapping(value = "/productServiceFallback", method = {RequestMethod.GET,
      RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<String> productServiceFallback() {
    log.info("Fallback productServiceFallback");
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FALLBACK_MESSAGE);
  }

  @RequestMapping(value = "/orderServiceFallback", method = {RequestMethod.GET, RequestMethod.POST,
      RequestMethod.PUT})
  public ResponseEntity<String> orderServiceFallback() {
    log.info("Fallback orderServiceFallback");
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FALLBACK_MESSAGE);
  }

  @RequestMapping(value = "/userServiceFallback", method = {RequestMethod.GET, RequestMethod.POST,
      RequestMethod.PUT})
  public ResponseEntity<String> userServiceFallback() {
    log.info("Fallback userServiceFallback");
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FALLBACK_MESSAGE);
  }

  @ExceptionHandler(NotAuthorizedException.class)
  public ResponseEntity<String> handleAuthenticationException(NotAuthorizedException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(INTERNAL_SERVER_ERROR_MESSAGE);
  }
}


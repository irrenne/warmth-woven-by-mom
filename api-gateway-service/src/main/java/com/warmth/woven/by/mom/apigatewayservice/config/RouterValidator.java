package com.warmth.woven.by.mom.apigatewayservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
public class RouterValidator {

  public static final List<String> openEndpoints = List.of(
      "/auth/register",
      "/auth/login",
      "/api/product/paged",
      "/api/product/random"
  );

  public Predicate<ServerHttpRequest> isSecured =
      request -> openEndpoints.stream()
          .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
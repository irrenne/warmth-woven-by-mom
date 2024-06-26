package com.warmth.woven.by.mom.apigatewayservice.config;

import com.warmth.woven.by.mom.apigatewayservice.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

  @Autowired
  private RouterValidator validator;

  @Autowired
  private JwtUtils jwtUtils;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    log.info("check request secured or nah");
    if (validator.isSecured.test(request)) {
      log.info("yes "+ request.getURI());
      if (authMissing(request)) {
        return onError(exchange);
      }

      final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

      if (jwtUtils.isExpired(token)) {
        return onError(exchange);
      }
    }
    return chain.filter(exchange);
  }

  private Mono<Void> onError(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return response.setComplete();
  }

  private boolean authMissing(ServerHttpRequest request) {
    return !request.getHeaders().containsKey("Authorization");
  }
}
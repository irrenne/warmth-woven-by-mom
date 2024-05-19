package com.warmth.woven.by.mom.apigatewayservice.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

  @Autowired
  private AuthenticationFilter filter;

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("PRODUCT-SERVICE", r -> r.path("/api/product/**")
            .filters(f -> f.filter(filter)
                .circuitBreaker(config -> config
                    .setName("productServiceCircuitBreaker")
                    .setFallbackUri("forward:/productServiceFallback")))
            .uri("lb://PRODUCT-SERVICE"))
        .route("ORDER-SERVICE", r -> r.path("/api/order/**")
            .filters(f -> f.filter(filter)
                .circuitBreaker(config -> config
                    .setName("orderServiceCircuitBreaker")
                    .setFallbackUri("forward:/orderServiceFallback")))
            .uri("lb://ORDER-SERVICE"))
        .route("USER-SERVICE", r -> r.path("/api/users/**")
            .filters(f -> f.filter(filter)
                .circuitBreaker(config -> config
                    .setName("userServiceCircuitBreaker")
                    .setFallbackUri("forward:/userServiceFallback")))
            .uri("lb://USER-SERVICE"))
        .route("AUTH-SERVICE", r -> r.path("/auth/**")
            .filters(f -> f.filter(filter)
                .circuitBreaker(config -> config
                    .setName("authServiceCircuitBreaker")
                    .setFallbackUri("forward:/userServiceFallback")))
            .uri("lb://AUTH-SERVICE"))
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:4200"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable);
    return http.build();
  }
}

package com.warmth.woven.by.mom.authenticationservice.controller;

import com.warmth.woven.by.mom.authenticationservice.dto.AuthRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.AuthResponse;
import com.warmth.woven.by.mom.authenticationservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping(value = "/register")
  public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping(value = "/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
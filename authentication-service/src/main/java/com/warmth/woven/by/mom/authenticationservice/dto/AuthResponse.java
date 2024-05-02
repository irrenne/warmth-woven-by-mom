package com.warmth.woven.by.mom.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

  private String accessToken;
  private String refreshToken;
}
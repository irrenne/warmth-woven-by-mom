package com.warmth.woven.by.mom.authenticationservice.service;

import com.warmth.woven.by.mom.authenticationservice.client.UserClient;
import com.warmth.woven.by.mom.authenticationservice.dto.AuthRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.AuthResponse;
import com.warmth.woven.by.mom.authenticationservice.dto.UserRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.UserResponse;
import com.warmth.woven.by.mom.authenticationservice.util.JwtUtil;

import jakarta.ws.rs.NotAuthorizedException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserClient userClient;
  private final JwtUtil jwtUtil;

  public AuthResponse register(AuthRequest request) {
    String userId = UUID.randomUUID().toString();
    request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    UserRequest userRequest = UserRequest.builder()
        .id(userId)
        .name(request.getName())
        .email(request.getEmail())
        .password(request.getPassword())
        .role("USER")
        .build();
    UserResponse registeredUser = userClient.registerUser(userRequest);

    log.info(
        String.format("User registered with id '%s' and role '%s'", registeredUser.getId(),
            registeredUser.getRole())
    );

    String accessToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(),
        "ACCESS");
    String refreshToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(),
        "REFRESH");

    return new AuthResponse(accessToken, refreshToken);
  }


  public AuthResponse login(AuthRequest request) {
    UserResponse user;
    try {
      user = userClient.getUserByEmail(request.getEmail());
    } catch (Exception e) {
      throw new NotAuthorizedException("Invalid email");
    }
    if (user.getPassword() == null || user.getPassword().isEmpty() || !BCrypt.checkpw(
        request.getPassword(), user.getPassword())) {
      throw new NotAuthorizedException("Invalid email/password");
    }

    String accessToken = jwtUtil.generate(user.getId(), user.getRole(), "ACCESS");
    String refreshToken = jwtUtil.generate(user.getId(), user.getRole(), "REFRESH");

    return new AuthResponse(accessToken, refreshToken);
  }
}
package com.warmth.woven.by.mom.authenticationservice.service;

import com.warmth.woven.by.mom.authenticationservice.client.UserClient;
import com.warmth.woven.by.mom.authenticationservice.dto.AuthRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.AuthResponse;
import com.warmth.woven.by.mom.authenticationservice.dto.UserRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.UserResponse;
import com.warmth.woven.by.mom.authenticationservice.util.JwtUtil;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserClient userClient;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private AuthRequest authRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("test@example.com", "password", "John Doe");
        UserRequest userRequest = new UserRequest(UUID.randomUUID().toString(), "John Doe", "test@example.com", BCrypt.hashpw("password", BCrypt.gensalt()), "USER");
        userResponse = new UserResponse(userRequest.getId(), userRequest.getName(), userRequest.getEmail(), userRequest.getPassword(), userRequest.getRole());
    }

    @Test
    void testRegister() {
        when(userClient.registerUser(any(UserRequest.class))).thenReturn(userResponse);
        when(jwtUtil.generate(any(), any(), any())).thenReturn("accessToken", "refreshToken");

        AuthResponse authResponse = authService.register(authRequest);

        assertNotNull(authResponse);
        assertEquals("accessToken", authResponse.getAccessToken());
        assertEquals("refreshToken", authResponse.getRefreshToken());
    }

    @Test
    void testLogin() {
        when(userClient.getUserByEmail(authRequest.getEmail())).thenReturn(userResponse);
        when(jwtUtil.generate(any(), any(), any())).thenReturn("accessToken", "refreshToken");

        AuthResponse authResponse = authService.login(authRequest);

        assertNotNull(authResponse);
        assertEquals("accessToken", authResponse.getAccessToken());
        assertEquals("refreshToken", authResponse.getRefreshToken());
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(userClient.getUserByEmail(authRequest.getEmail())).thenThrow(new NotAuthorizedException("Invalid email"));

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class, () -> authService.login(authRequest));

        assertEquals("HTTP 401 Unauthorized", exception.getMessage());
    }

    @Test
    void testLoginWithInvalidPassword() {
        when(userClient.getUserByEmail(authRequest.getEmail())).thenReturn(userResponse);

        authRequest.setPassword("wrongPassword");

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class, () -> authService.login(authRequest));

        assertEquals("HTTP 401 Unauthorized", exception.getMessage());
    }
}

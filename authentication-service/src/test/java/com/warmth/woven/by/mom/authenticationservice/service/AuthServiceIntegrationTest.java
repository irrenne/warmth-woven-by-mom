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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserClient userClient;

    @Autowired
    private JwtUtil jwtUtil;

    private AuthRequest authRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("test@example.com", "password", "John Doe");
        userResponse = new UserResponse("1", "John Doe", "test@example.com", BCrypt.hashpw("password", BCrypt.gensalt()), "USER");
    }

    @Test
    void testRegister() {
        when(userClient.registerUser(any(UserRequest.class))).thenReturn(userResponse);

        AuthResponse authResponse = authService.register(authRequest);

        assertNotNull(authResponse);
        assertNotNull(authResponse.getAccessToken());
        assertNotNull(authResponse.getRefreshToken());
    }

    @Test
    void testLogin() {
        when(userClient.getUserByEmail(anyString())).thenReturn(userResponse);

        AuthResponse authResponse = authService.login(authRequest);

        assertNotNull(authResponse);
        assertNotNull(authResponse.getAccessToken());
        assertNotNull(authResponse.getRefreshToken());
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(userClient.getUserByEmail(anyString())).thenThrow(new NotAuthorizedException("Invalid email"));

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class, () -> authService.login(authRequest));
        assertEquals("HTTP 401 Unauthorized", exception.getMessage());
    }

    @Test
    void testLoginWithInvalidPassword() {
        userResponse.setPassword(BCrypt.hashpw("wrongPassword", BCrypt.gensalt()));
        when(userClient.getUserByEmail(anyString())).thenReturn(userResponse);

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class, () -> authService.login(authRequest));
        assertEquals("HTTP 401 Unauthorized", exception.getMessage());
    }
}

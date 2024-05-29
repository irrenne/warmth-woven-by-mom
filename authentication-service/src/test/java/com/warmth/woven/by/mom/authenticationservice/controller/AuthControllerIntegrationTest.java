package com.warmth.woven.by.mom.authenticationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.authenticationservice.client.UserClient;
import com.warmth.woven.by.mom.authenticationservice.dto.AuthRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.UserRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.UserResponse;
import com.warmth.woven.by.mom.authenticationservice.util.JwtUtil;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testRegister() throws Exception {
        when(userClient.registerUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void testLogin() throws Exception {
        when(userClient.getUserByEmail(anyString())).thenReturn(userResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void testLoginWithInvalidEmail() throws Exception {
        when(userClient.getUserByEmail(anyString())).thenThrow(new NotAuthorizedException("Invalid email"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("HTTP 401 Unauthorized")));
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        userResponse.setPassword(BCrypt.hashpw("wrongPassword", BCrypt.gensalt()));
        when(userClient.getUserByEmail(anyString())).thenReturn(userResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("HTTP 401 Unauthorized")));
    }
}

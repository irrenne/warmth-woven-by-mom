package com.warmth.woven.by.mom.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.userservice.dto.UserBasicInfoResponse;
import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;
    private UserBasicInfoResponse userBasicInfoResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse("1", "John Doe", "test@example.com", "password", "USER");
        userBasicInfoResponse = new UserBasicInfoResponse("John Doe", "test@example.com");
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(anyString())).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/email/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetUserBasicInfoById() throws Exception {
        when(userService.getUserBasicInfoById(anyString())).thenReturn(userBasicInfoResponse);

        mockMvc.perform(get("/api/users/basic-info/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest("1", "John Doe", "test@example.com", "password", "USER");
        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserRequest userRequest = new UserRequest("1", "John Doe", "test@example.com", "password", "USER");
        when(userService.updateUser(anyString(), any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/api/users/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}

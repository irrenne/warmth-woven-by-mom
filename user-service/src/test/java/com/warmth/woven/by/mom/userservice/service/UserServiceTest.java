package com.warmth.woven.by.mom.userservice.service;

import com.warmth.woven.by.mom.userservice.dto.UserBasicInfoResponse;
import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.model.User;
import com.warmth.woven.by.mom.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");

        userRequest = new UserRequest();
        userRequest.setId("1");
        userRequest.setName("John Doe");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setRole("USER");
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserResponse result = userService.getUserById("1");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserResponse result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    void testCreateUser() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsException() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));

        assertEquals("User with this email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.existsById("1")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.updateUser("1", userRequest);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserThrowsException() {
        when(userRepository.existsById("1")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.updateUser("1", userRequest));

        assertEquals("User not found with id '1'", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserBasicInfoById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserBasicInfoResponse result = userService.getUserBasicInfoById("1");

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }
}

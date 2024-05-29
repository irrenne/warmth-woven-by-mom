package com.warmth.woven.by.mom.userservice.service;

import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.model.User;
import com.warmth.woven.by.mom.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setId("1");
        userRequest.setName("John Doe");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setRole("USER");

        user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");

        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        UserResponse userResponse = userService.createUser(userRequest);

        assertNotNull(userResponse);
        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getName(), userResponse.getName());
        assertEquals(user.getEmail(), userResponse.getEmail());
    }

    @Test
    void testGetUserById() {
        User savedUser = userRepository.save(user);

        UserResponse userResponse = userService.getUserById("1");

        assertNotNull(userResponse);
        assertEquals(savedUser.getId(), userResponse.getId());
        assertEquals(savedUser.getEmail(), userResponse.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        User savedUser = userRepository.save(user);

        UserResponse userResponse = userService.getUserByEmail("test@example.com");

        assertNotNull(userResponse);
        assertEquals(savedUser.getId(), userResponse.getId());
        assertEquals(savedUser.getEmail(), userResponse.getEmail());
    }

    @Test
    void testUpdateUser() {
        userRepository.save(user);

        userRequest.setName("John Smith");
        UserResponse userResponse = userService.updateUser("1", userRequest);

        assertNotNull(userResponse);
        assertEquals(user.getId(), userResponse.getId());
        assertEquals(userRequest.getName(), userResponse.getName());
    }

    @Test
    void testGetUserBasicInfoById() {
        User savedUser = userRepository.save(user);

        UserResponse userResponse = userService.getUserById("1");

        assertNotNull(userResponse);
        assertEquals(savedUser.getId(), userResponse.getId());
        assertEquals(savedUser.getEmail(), userResponse.getEmail());
    }
}

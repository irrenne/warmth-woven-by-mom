package com.warmth.woven.by.mom.userservice.controller;

import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public UserResponse getUserById(@PathVariable String id) {
    return userService.getUserById(id);
  }

  @GetMapping("/{email}")
  public UserResponse getUserByEmail(@PathVariable String email) {
    return userService.getUserByEmail(email);
  }

  @PostMapping
  public UserResponse createUser(@RequestBody UserRequest userRequest) {
    return userService.createUser(userRequest);
  }

  @PutMapping
  public UserResponse updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
    return userService.updateUser(id, userRequest);
  }
}

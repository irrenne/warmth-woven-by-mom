package com.warmth.woven.by.mom.userservice.controller;

import com.warmth.woven.by.mom.userservice.dto.UserBasicInfoResponse;
import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public UserResponse getUserById(@PathVariable String id) {
    return userService.getUserById(id);
  }

  @GetMapping("/email/{email}")
  public UserResponse getUserByEmail(@PathVariable String email) {
    return userService.getUserByEmail(email);
  }

  @GetMapping("/basic-info/{id}")
  public UserBasicInfoResponse getUserBasicInfoById(@PathVariable String id) {
    return userService.getUserBasicInfoById(id);
  }

  @PostMapping
  public UserResponse createUser(@RequestBody UserRequest userRequest) {
    return userService.createUser(userRequest);
  }

  @PutMapping("/{id}")
  public UserResponse updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
    return userService.updateUser(id, userRequest);
  }
}

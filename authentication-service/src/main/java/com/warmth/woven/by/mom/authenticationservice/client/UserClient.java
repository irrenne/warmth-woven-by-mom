package com.warmth.woven.by.mom.authenticationservice.client;

import com.warmth.woven.by.mom.authenticationservice.dto.UserRequest;
import com.warmth.woven.by.mom.authenticationservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

  @PostMapping("/api/users")
  UserResponse registerUser(@RequestBody UserRequest userRequest);

  @GetMapping("/api/users/email/{email}")
  UserResponse getUserByEmail(@PathVariable String email);
}

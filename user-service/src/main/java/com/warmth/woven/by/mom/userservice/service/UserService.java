package com.warmth.woven.by.mom.userservice.service;

import com.warmth.woven.by.mom.userservice.dto.UserBasicInfoResponse;
import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.model.User;
import com.warmth.woven.by.mom.userservice.repository.UserRepository;
import com.warmth.woven.by.mom.userservice.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse getUserById(String id) {
    User user = userRepository.findById(id)
        .orElse(null);
    return UserMapper.INSTANCE.mapUserToUserResponse(user);
  }

  public UserResponse getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElse(null);
    return UserMapper.INSTANCE.mapUserToUserResponse(user);
  }

  public UserResponse createUser(UserRequest userRequest) {
    User user = UserMapper.INSTANCE.mapUserRequestToUser(userRequest);
    if (userRepository.existsByEmail(userRequest.getEmail())) {
      throw new RuntimeException("User with this email already exists");
    }
    User savedUser = userRepository.save(user);
    log.info(
        String.format("User created with id '%s' and role '%s'", user.getId(), user.getRole())
    );
    return UserMapper.INSTANCE.mapUserToUserResponse(savedUser);
  }

  public UserResponse updateUser(String id, UserRequest userRequest) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException(String.format("User not found with id '%s'", id));
    }

    User user = UserMapper.INSTANCE.mapUserRequestToUser(userRequest);
    user.setId(id);
    User savedUser = userRepository.save(user);
    return UserMapper.INSTANCE.mapUserToUserResponse(savedUser);
  }

  public UserBasicInfoResponse getUserBasicInfoById(String id) {
    User user = userRepository.findById(id)
        .orElse(null);
    return UserMapper.INSTANCE.mapUserToUserBasicInfoResponse(user);
  }
}

package com.warmth.woven.by.mom.userservice.util.mapper;

import com.warmth.woven.by.mom.userservice.dto.UserRequest;
import com.warmth.woven.by.mom.userservice.dto.UserResponse;
import com.warmth.woven.by.mom.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User mapUserRequestToUser(UserRequest userRequest);

  @Mapping(target = "password", source = "password")
  UserResponse mapUserToUserResponse(User user);
}

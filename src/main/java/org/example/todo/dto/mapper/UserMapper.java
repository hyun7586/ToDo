package org.example.todo.dto.mapper;

import org.example.todo.domain.UserEntity;
import org.example.todo.dto.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserResponse toResponse(UserEntity entity){
    return UserResponse.builder()
        .userId(entity.getUserId())
        .userName(entity.getUsername())
        .userEmail(entity.getUserEmail())
        .build();
  }

}

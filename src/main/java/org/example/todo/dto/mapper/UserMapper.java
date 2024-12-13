package org.example.todo.dto.mapper;

import org.example.todo.domain.UserEntity;
import org.example.todo.dto.user.UserRequest;
import org.example.todo.dto.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserResponse toResponse(UserEntity entity){
    return UserResponse.builder()
        .userId(entity.getUserId())
        .userName(entity.getUsername())
        .userEmail(entity.getUserEmail())
        .build();
  }

  public UserEntity toEntity(UserRequest request){
    return UserEntity.builder()
        .userName(request.getUserName())
        .userEmail(request.getUserEmail())
        .userPassword(bCryptPasswordEncoder.encode(request.getUserPassword()))
        .build();
  }

}

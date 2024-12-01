package org.example.todo.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

  private Long userId;
  private String userName;
  private String userEmail;

}

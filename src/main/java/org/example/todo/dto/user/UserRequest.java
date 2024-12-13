package org.example.todo.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
  private String userName;
  private String userEmail;
  private String userPassword;

}

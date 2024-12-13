package org.example.todo.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

@Tag(name = "auth api", description = "user login 관련 인증 api")
public interface UserAuthSpec {

  @Operation(
      summary = "user login",
      description = "사용자 인증, 토큰 발급, mainPage redirect까지 담당하는 api"
  )
  ResponseEntity<?> login(String email, String password);

  @Operation(
      summary = "user signup",
      description = "signUp page에서 입력받은 값에 따라 user 생성하는 api"
  )
  ResponseEntity<?> signUpUser(String username, String email, String password);

  @Operation(
      summary = "user logout"
  )
  ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

}

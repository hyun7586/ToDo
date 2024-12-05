package org.example.todo.controller.token;

import lombok.RequiredArgsConstructor;
import org.example.todo.dto.token.CreateAccessTokenRequest;
import org.example.todo.dto.token.CreateAccessTokenResponse;
import org.example.todo.service.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

  private final TokenService tokenService;

  // 주어진 경로로 refreshToken을 주면 accessToken 발급을 요청하는 api
  // parameter로 주어진 request에는 String refreshToken field만 존재함.
  @PostMapping("/api/token")
  public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken
      (@RequestBody CreateAccessTokenRequest request) {
    String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CreateAccessTokenResponse.builder()
            .accessToken(newAccessToken)
            .build());
  }
}

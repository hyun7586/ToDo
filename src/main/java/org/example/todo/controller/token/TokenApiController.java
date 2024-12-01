package org.example.todo.controller.token;

import lombok.RequiredArgsConstructor;
import org.example.todo.dto.token.CreateAccessTokenRequest;
import org.example.todo.dto.token.CreateAccessTokenResponse;
import org.example.todo.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

  private final TokenService tokenService;

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

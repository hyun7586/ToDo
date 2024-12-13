package org.example.todo.controller.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.dto.token.CreateAccessTokenRequest;
import org.example.todo.dto.token.CreateAccessTokenResponse;
import org.example.todo.service.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/token/api")
public class TokenApiController {

  private final TokenService tokenService;

  @PostMapping("")
  public ResponseEntity<?> createNewAccessToken
      (@RequestBody CreateAccessTokenRequest request) {
    String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

    // tokenService.createNewAccessToken()이 null을 return하는 경우는 없긴 함.
    if (newAccessToken == null) {
      log.error("you are cooked!: tokenService.createNewAccessToken() return null");
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body("error: tokenService.createNewAccessToken() return null");
    }

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CreateAccessTokenResponse.builder()
            .accessToken(newAccessToken)
            .build());
  }
}

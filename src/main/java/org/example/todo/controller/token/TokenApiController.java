package org.example.todo.controller.token;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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
@Slf4j
public class TokenApiController {

  private final TokenService tokenService;

  @PostMapping("/api/token")
  public ResponseEntity<?> createNewAccessToken
      (@RequestBody CreateAccessTokenRequest request) {
    try {
      String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

      // tokenService.createNewAccessToken()이 null을 return하는 경우는 없긴 함.
      if(newAccessToken==null){
        log.error("you are cooked!: tokenService.createNewAccessToken() return null");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("error: tokenService.createNewAccessToken() return null");
      }

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(CreateAccessTokenResponse.builder()
              .accessToken(newAccessToken)
              .build());

    } catch(ExpiredJwtException e){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("the refresh token is expired. Please login again.");
    } catch(Exception e){
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body("Unexpected Exception: "+e.getMessage());
    }
  }
}

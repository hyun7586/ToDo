package org.example.todo.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.example.todo.config.jwt.TokenProvider;
import org.example.todo.domain.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  public String createNewAccessToken(String refreshToken){

    if(!tokenProvider.validToken(refreshToken)){
      throw new IllegalArgumentException("Unexpected token");
    }

    // find userId, User
    Long userId = tokenProvider.getUserId(refreshToken);
    UserEntity user = userService.findByIdAndReturnUserEntity(userId);

    // generate Access Token by the information found before
    return tokenProvider.generateToken(user, Duration.ofDays(1));
  }

}

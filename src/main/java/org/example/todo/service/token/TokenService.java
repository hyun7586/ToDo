package org.example.todo.service.token;

import io.jsonwebtoken.Jwts;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.config.jwt.JwtProperties;
import org.example.todo.config.jwt.TokenProvider;
import org.example.todo.domain.UserEntity;
import org.example.todo.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

  private final TokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;
  private final JwtProperties jwtProperties;

  public String createNewAccessToken(String refreshToken){

    if(!tokenProvider.validToken(refreshToken)){
      log.info("Secret Key: {}", jwtProperties.getSecretKey());
      throw new IllegalArgumentException("Unexpected token");
    }

    // find userId, User
    Long userId = tokenProvider.getUserId(refreshToken);
    UserEntity user = userService.findByIdAndReturnUserEntity(userId);

    // generate Access Token by the information found before
    return tokenProvider.generateToken(user, Duration.ofHours(2));
  }

  public String createNewRefreshToken(UserEntity user){
    return tokenProvider.generateToken(user, Duration.ofDays(2));
  }

}

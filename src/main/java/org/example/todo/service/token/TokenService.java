package org.example.todo.service.token;

import io.jsonwebtoken.ExpiredJwtException;
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

  public String createNewAccessToken(String refreshToken) {

    try {
      if (tokenProvider.validToken(refreshToken)) {

        Long userId = tokenProvider.getUserId(refreshToken);
        UserEntity user = userService.findByIdAndReturnUserEntity(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
      }
    } catch (ExpiredJwtException e) {
      log.error("RefreshToken is expired");
      throw e;
    } catch (Exception e) {
      log.error("Unexpected Exception: {}", e.getMessage());
      throw e;
    }

    // tokenProvider.validToken()은 true를 return하지 않으면 반드시 Exception을 발생시켜 위에서 catch되기
    // 때문에 아래 return null을 수행하지는 않을 거임.
    return null;
  }

  public String createNewRefreshToken(UserEntity user) {
    return tokenProvider.generateToken(user, Duration.ofDays(2));
  }

  // generateToken for admin
  // it can be user to generate refresh/accessToken
  public String createNewTokenAdmin(UserEntity user, Duration duration){
    return tokenProvider.generateToken(user, duration);
  }


}

package org.example.todo.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.domain.UserEntity;
import org.example.todo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class TokenProviderTest {

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtProperties jwtProperties;

  @DisplayName("generateToken(): 유저 정보와 만료기간을 전달해 토큰을 만들 수 있다")
  @Test
  void generateToken(){
    UserEntity testUser = UserEntity.builder()
        .userEmail("test@gmail.com")
        .userPassword("test password")
        .build();

    userRepository.save(testUser);

    String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

    Long userId = Jwts.parserBuilder()
        .setSigningKey(jwtProperties.getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("id", Long.class);

    assertThat(userId).isEqualTo(testUser.getUserId());
  }

}

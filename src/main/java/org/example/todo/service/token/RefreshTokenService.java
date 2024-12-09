package org.example.todo.service.token;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.RefreshToken;
import org.example.todo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken findByUserId(Long userId){
    return refreshTokenRepository.findByUserId(userId).orElse(null);
  }

  // refreshToken String 값으로 RefreshToken 객체 가져오기
  public RefreshToken findByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
  }

  public RefreshToken save(RefreshToken token){
    return refreshTokenRepository.save(token);
  }

}

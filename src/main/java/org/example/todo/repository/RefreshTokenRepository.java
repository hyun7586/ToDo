package org.example.todo.repository;

import java.util.Optional;
import org.example.todo.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUserId(Long userId);

  // refreshToken string 값을 이용해서 RefreshToken 객체를 가져옴
  Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

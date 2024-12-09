package org.example.todo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name="refresh_token")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id", updatable=false)
  private Long id;

  @Column(name="user_id", nullable = false, unique = true)
  private Long userId;

  @Column(name="refresh_token", nullable = false)
  private String refreshToken;

  public RefreshToken(Long userId, String refreshToken){
    this.userId = userId;
    this.refreshToken = refreshToken;
  }

  public RefreshToken update(String newRefreshToken){
    this.refreshToken = newRefreshToken;
    return this;
  }

}

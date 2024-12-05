package org.example.todo.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenProvider {

  private final JwtProperties jwtProperties;

  private Key getSecretKey() {
    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)));
  }

  public String generateToken(UserEntity user, Duration expiryDuration) {
    Date now = new Date();

    return makeToken(new Date(now.getTime() + expiryDuration.toMillis()), user);
  }

  public String makeToken(Date expiredAt, UserEntity user) {
    Date now = new Date();

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setSubject(user.getUserEmail())
        .setIssuer(jwtProperties.getIssuer())
        .setIssuedAt(now)
        .setExpiration(expiredAt)
        .claim("id", user.getUserId())
        .signWith(SignatureAlgorithm.HS256, getSecretKey())
        .compact();
  }


  public boolean validToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSecretKey())
          .build()
          .parseClaimsJws(token);

      return true;
    } catch (Exception e) {
      return false;
    }
  }


  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities = Collections.singleton(
        new SimpleGrantedAuthority("ROLE_USER"));

    return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities),
        token, authorities);
  }

  public Long getUserId(String token) {
    Claims claims = getClaims(token);

    return claims.get("id", Long.class);
  }

  // String type의 token을 복호화해서 body를 추출하는 method
  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

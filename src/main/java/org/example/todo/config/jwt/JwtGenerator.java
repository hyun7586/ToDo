package org.example.todo.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

  public String generateAccessToken(final Key ACCESS_SECRET, final Long ACCESS_EXPIRATION, User user){
    Long now = System.currentTimeMillis();

    return Jwts.builder()
        .setHeader(createHeader())
        .setClaims(createClaims(user))
        .setSubject(user.getUsername())
        .setExpiration(new Date(now + ACCESS_EXPIRATION))
        .signWith(ACCESS_SECRET, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(final Key REFRESH_SECRET, final Long REFRESH_EXPIRATION, User user){
    Long now = System.currentTimeMillis();

    return Jwts.builder()
        .setHeader(createHeader())
        .setSubject(user.getUsername())
        .setClaims(createClaims(user))
        .setExpiration(new Date(now + REFRESH_EXPIRATION))
        .signWith(REFRESH_SECRET, SignatureAlgorithm.HS256)
        .compact();
  }

  // create header
  private Map<String, Object> createHeader(){
    Map<String, Object> temp = new HashMap<>();
    temp.put("typ", "JWT");
    temp.put("alg", "HS256");

    return temp;
  }

  // create cliams(user)
  private Map<String, Object> createClaims(User user){
    Map<String, Object> temp = new HashMap<>();
    temp.put("username", user.getUsername());
    temp.put("roles", user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList());

    return temp;
  }

}

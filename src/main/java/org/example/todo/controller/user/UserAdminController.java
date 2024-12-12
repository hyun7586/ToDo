package org.example.todo.controller.user;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.domain.RefreshToken;
import org.example.todo.domain.UserEntity;
import org.example.todo.service.token.RefreshTokenService;
import org.example.todo.service.token.TokenService;
import org.example.todo.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class UserAdminController {

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final RefreshTokenService refreshTokenService;

  @GetMapping("/admin-login")
  public ResponseEntity<?> adminLogin(
      @RequestParam String email,
      @RequestParam String password,
      @RequestParam Integer expiryDays
  ) {
    try {
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(email, password);
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserEntity user = userService.findByEmailAndReturnUserEntity(email);

      String refreshToken = tokenService.createNewTokenAdmin(user, Duration.ofDays(expiryDays));
      String accessToken = tokenService.createNewTokenAdmin(user, Duration.ofDays(expiryDays));

      RefreshToken target = refreshTokenService.findByUserId(user.getUserId());
      if (target == null) {
        log.info("new refresh Token: {}",
            refreshTokenService.save(new RefreshToken(user.getUserId(), refreshToken)));
      } else {
        target.setRefreshToken(refreshToken);
        log.info("the refresh token is updated: {}", refreshTokenService.save(target));
      }

      return ResponseEntity.status(HttpStatus.OK)
          .body(accessToken);
    } catch(Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unexpected Exception: "+ e.getMessage());
    }
  }
}

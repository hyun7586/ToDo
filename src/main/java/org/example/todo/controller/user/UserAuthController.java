package org.example.todo.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.domain.RefreshToken;
import org.example.todo.domain.UserEntity;
import org.example.todo.dto.user.UserRequest;
import org.example.todo.service.token.RefreshTokenService;
import org.example.todo.service.token.TokenService;
import org.example.todo.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAuthController implements UserAuthSpec {

  private final UserService userService;
  private final TokenService tokenService;
  private final RefreshTokenService refreshTokenService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestParam String email,
      @RequestParam String password
  ) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserEntity user = userService.findByEmailAndReturnUserEntity(email);
    if (user == null) {
      throw new NoSuchElementException();
    }

    String refreshToken = tokenService.createNewRefreshToken(user);
    String accessToken = tokenService.createNewAccessToken(refreshToken);
    log.debug("accessToken: {}", accessToken);

    // save or update
    RefreshToken target = refreshTokenService.findByUserId(user.getUserId());
    if (target == null) {
      log.info("new refresh Token saved! {}",
          refreshTokenService.save(new RefreshToken(user.getUserId(), refreshToken)));
    } else {
      target.setRefreshToken(refreshToken);
      log.info("original token is updated! {}", refreshTokenService.save(target));
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(accessToken);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signUpUser(
      @RequestParam String username,
      @RequestParam String email,
      @RequestParam String password
  ) {
    Long savedId = userService.save(UserRequest.builder()
        .userEmail(email)
        .userName(username)
        .userPassword(password)
        .build());

    return ResponseEntity.status(HttpStatus.OK)
        .body(savedId);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

    new SecurityContextLogoutHandler().logout(request, response,
        SecurityContextHolder.getContext().getAuthentication());

    return ResponseEntity.status(HttpStatus.OK)
        .body("log-out is completed");
  }

}

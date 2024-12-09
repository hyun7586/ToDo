package org.example.todo.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Ref;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.todo.domain.RefreshToken;
import org.example.todo.domain.UserEntity;
import org.example.todo.dto.user.UserAddRequest;
import org.example.todo.repository.RefreshTokenRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
      @RequestParam String password,
      Model model
  ) {
    try {
      // token들이 만료되지 않은 사용자가 재로그인하더라도 그냥 토큰 둘 다 새로 발급

      // 1. 사용자 인증 시도
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(email, password);
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 2. 사용자 entity 조회
      UserEntity user = userService.findByEmailAndReturnUserEntity(email);

      if(user==null){
        log.error("the user is not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("the user is not found");
      }

      // 3. 토근 발급
      String refreshToken = tokenService.createNewRefreshToken(user);
      String accessToken = tokenService.createNewAccessToken(refreshToken);

      // 4. accessToken log
      log.info("accessToken: {}", accessToken);

      // 5. 변경된 refreshToken 정보로 기존 refreshToken을 update or 새로 저장
      RefreshToken target = refreshTokenService.findByUserId(user.getUserId());
      if(target==null){
        log.info("new refresh Token saved! {}",refreshTokenService.save(new RefreshToken(user.getUserId(), refreshToken)));
      } else{
        target.setRefreshToken(refreshToken);
        log.info("original token is updated! {}",refreshTokenService.save(target));
      }


      // 6. 로그인 성공 시 main page로 redirect
      return ResponseEntity.status(HttpStatus.OK)
          .body(accessToken);
    } catch (Exception e) {

      // 6. 로그인 실패 처리
      log.error("Unexpected Exception: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unexpected Exception: "+e.getMessage());
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signUpUser(
      @RequestParam String username,
      @RequestParam String email,
      @RequestParam String password
  ) {
    Long savedId;
    try {
      UserAddRequest user = UserAddRequest.builder()
          .userEmail(email)
          .userName(username)
          .userPassword(password)
          .build();

      savedId = userService.save(user);
    } catch (Exception e) {
      log.error("Unexpected Exception: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unexpected Exception: " + e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(savedId);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    try {
      new SecurityContextLogoutHandler().logout(request, response,
          SecurityContextHolder.getContext().getAuthentication());
    } catch (Exception e){
      log.error("Unexpected Exception: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unexpected Exception: "+ e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body("log-out is completed");
  }

}

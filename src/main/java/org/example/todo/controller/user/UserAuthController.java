package org.example.todo.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.domain.RefreshToken;
import org.example.todo.domain.UserEntity;
import org.example.todo.dto.user.UserAddRequest;
import org.example.todo.repository.RefreshTokenRepository;
import org.example.todo.service.token.TokenService;
import org.example.todo.service.user.UserService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserAuthController implements UserAuthSpec{

  private final UserService userService;
  private final TokenService tokenService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public String login(
      @RequestParam String email,
      @RequestParam String password,
      Model model
  ) {
    try {
      // 1. 사용자 인증 시도
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(email, password);
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 2. 사용자 entity 조회
      UserEntity user = userService.findByEmailAndReturnUserEntity(email);

      // 3. 토큰 발급
      String refreshToken = tokenService.createNewRefreshToken(user);
      String accessToken = tokenService.createNewAccessToken(refreshToken);

      // 4. accessToken은 client에 return
      model.addAttribute("accessToken", accessToken);

      // 5. refreshToken은 DB에 저장
      refreshTokenRepository.save(new RefreshToken(user.getUserId(), refreshToken));

      // 6. 로그인 성공 시 main page로 redirect
      return "redirect:/mainPage";
    } catch (Exception e){

      // 6. 로그인 실패 처리
       model.addAttribute("error", "Invalid email or password");
       return "redirect:/login?error=true";
    }
  }

  // login, signup, logout
  @PostMapping("/signup")
  public String signUpUser(
      @RequestParam String username,
      @RequestParam String email,
      @RequestParam String password
  ) {
    UserAddRequest user = UserAddRequest.builder()
        .userEmail(email)
        .userName(username)
        .userPassword(password)
        .build();

    userService.save(user);

    return "redirect:/login";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response,
        SecurityContextHolder.getContext().getAuthentication());

    return "redirect:/login";
  }

}

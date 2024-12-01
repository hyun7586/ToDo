package org.example.todo.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.todo.dto.user.UserAddRequest;
import org.example.todo.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserLoginController {

  private final UserService userService;

  // login, signup, logout
  @PostMapping("/signup")
  public String signUpUser(
      @RequestBody UserAddRequest request
  ) {
    userService.save(request);

    return "redirect:/login";
  }

  // login은 api 따로 구현하지 않아도 됨.
  // WebSecurityConfig에서 formlogin 설정을 해 놨으면 security가 알아서 login 처리함

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response,
        SecurityContextHolder.getContext().getAuthentication());

    return "redirect:/login";
  }

}

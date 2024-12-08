package org.example.todo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

  @GetMapping("/login")
  public String login(){
    return "login";
  }

  @GetMapping("/signup")
  public String signup(){
    return "signup";
  }

  @GetMapping("/mainPage")
  public String mainPage(){
    return "mainPage";
  }
}

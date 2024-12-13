package org.example.todo.controller.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule/view")
public class ScheduleViewController {

  @GetMapping("/main")
  public String mainPage(){
    return "main";
  }

}

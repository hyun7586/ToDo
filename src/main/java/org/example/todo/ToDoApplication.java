package org.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class ToDoApplication {

  public static void main(String[] args) {
    SpringApplication.run(ToDoApplication.class, args);
  }

}

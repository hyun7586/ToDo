package org.example.todo.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "org.example.todo.controller.schedule")
public class ScheduleExceptionHandler {

  @ExceptionHandler(value={Exception.class})
  public ResponseEntity<String> exceptionHandler(
      Exception e
  ){
    log.error("Unexpected Exception: {}", e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Unexpected Exception: "+e.getMessage());
  }
}

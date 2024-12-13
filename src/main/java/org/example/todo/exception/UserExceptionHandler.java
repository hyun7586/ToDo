package org.example.todo.exception;


import io.jsonwebtoken.JwtException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.controller.user.UserAdminController;
import org.example.todo.controller.user.UserAuthController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "org.example.todo.controller.user")
public class UserExceptionHandler {

  @ExceptionHandler(value={NoSuchElementException.class, NullPointerException.class})
  public ResponseEntity<?> noElementHandler(
      Exception e
  ){
    log.error("No such element exception: {}", e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("the element is not found");
  }

  @ExceptionHandler(value={IllegalArgumentException.class})
  public ResponseEntity<String> illegalArgumentHandler(
      IllegalArgumentException e
  ){
    log.error("Illegal argument exception: {}", e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("it is illegal arguments");
  }

  @ExceptionHandler(value={JwtException.class})
  public ResponseEntity<String> jwtHandler(
      JwtException e
  ){
    log.error("invalid jwt. jwt problem.");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("it is a jwt problem");
  }

  @ExceptionHandler(value={Exception.class})
  public ResponseEntity<String> globalHandler(
      Exception e
  ){
    log.error("Unexpected Exception: {}", e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Unexpected Exception: "+e.getMessage());
  }

}


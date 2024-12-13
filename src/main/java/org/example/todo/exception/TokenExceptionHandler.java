package org.example.todo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.todo.controller.token")
@Slf4j
public class TokenExceptionHandler{

  @ExceptionHandler(value={ExpiredJwtException.class})
  public ResponseEntity<String> expiredHandler(
      ExpiredJwtException e
  ){
    log.error("Jwt token is expired: {}", e.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Jwt token is expired");
  }

  @ExceptionHandler(value={Exception.class})
  public ResponseEntity<String> exceptionHandler(
      Exception e
  ){
    log.error("Unexpected Exception: {}", e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Unexpected Exception: "+e.getMessage());
  }

}

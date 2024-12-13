package org.example.todo.controller.user;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.ScheduleEntity;
import org.example.todo.domain.UserEntity;
import org.example.todo.dto.mapper.UserMapper;
import org.example.todo.dto.user.UserRequest;
import org.example.todo.dto.user.UserResponse;
import org.example.todo.service.schedule.ScheduleService;
import org.example.todo.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
public class UserApiController implements UserApiSpec {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserMapper userMapper;

  @GetMapping("/modify_for_admin")
  public ResponseEntity<?> modify_for_admin() {
    return ResponseEntity.status(HttpStatus.OK)
        .body("modify_for_admin(): this method is empty now");
  }

  @GetMapping("/all")
  public ResponseEntity<?> findAllUsers() {
    List<UserResponse> result = userService.findAll();

    return ResponseEntity.status(HttpStatus.OK)
        .body(result);
  }


  @GetMapping("/{user_id}")
  public ResponseEntity<?> findUserById(
      @PathVariable(name = "user_id") Long user_id
  ) {
    UserResponse target = userService.findById(user_id);

    if (target == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the user is not found");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(target);
  }


  @PostMapping("/post")
  public ResponseEntity<?> addUser(
      @RequestBody UserRequest request
  ) {
    Long target = userService.save(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.findById(target));
  }


  @PatchMapping("/update/{user_id}")
  public ResponseEntity<?> updateUser(
      @PathVariable(name = "user_id") Long user_id,
      @RequestBody UserRequest request
  ) {
    UserEntity target = userService.findByIdAndReturnUserEntity(user_id);

    if (target == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the user is not found");
    }

    if (request.getUserName() != null) {
      target.setUserName(request.getUserName());
    }

    if (request.getUserEmail() != null) {
      target.setUserEmail(request.getUserEmail());
    }

    if (request.getUserPassword() != null) {
      target.setUserPassword(bCryptPasswordEncoder.encode(request.getUserPassword()));
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(userMapper.toResponse(target));
  }


  @DeleteMapping("/delete/{user_id}")
  public ResponseEntity<?> deleteUser(
      @PathVariable(name = "user_id") Long user_id
  ) {
    if (userService.findById(user_id) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the user is not found");
    }

    userService.deleteById(user_id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(null);
  }
}

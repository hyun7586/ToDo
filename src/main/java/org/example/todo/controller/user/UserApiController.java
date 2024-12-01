package org.example.todo.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.todo.domain.UserEntity;
import org.example.todo.dto.user.UserAddRequest;
import org.example.todo.dto.user.UserLoginRequest;
import org.example.todo.dto.user.UserResponse;
import org.example.todo.dto.user.UserUpdateRequest;
import org.example.todo.repository.UserRepository;
import org.example.todo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
@Tag(name = "User management")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "success"),
    @ApiResponse(responseCode = "201", description = "successfully add the record"),
    @ApiResponse(responseCode = "404", description = "the resource is not found")
})
public class UserApiController {

  private final UserService userService;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Operation(
      summary = "find all user list for normal user",
      description = "view the particular fields of the user(except password)"
  )
  @GetMapping("/all")
  public ResponseEntity<?> findAllUsers() {
    List<UserResponse> result = userService.findAll();

    return ResponseEntity.status(HttpStatus.OK)
        .body(result);
  }


  @Operation(
      summary = "find the user by id",
      description = "view Id, Name, Email"
  )
  @GetMapping("/{user_id}")
  public ResponseEntity<?> findUserById(
      @PathVariable(name = "user_id", required = true) Long user_id
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
      @RequestBody UserAddRequest request
  ) {
    Long target = userService.save(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.findById(target));
  }


  @PatchMapping("/update/{user_id}")
  public ResponseEntity<?> updateUser(
      @PathVariable(name = "user_id", required = true) Long user_id,
      @RequestBody UserUpdateRequest request
  ) {
    UserResponse target = userService.findById(user_id);

    if (target == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the user is not found");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(target);
  }


  @DeleteMapping("/delete/{user_id}")
  public ResponseEntity<?> deleteUser(
      @PathVariable(name = "user_id", required = true) Long user_id
  ) {
    userService.deleteById(user_id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(null);
  }


  // for admin, modify DB, logic ... etc
  // this address doesn't need to be authenticated, it's permitted all.

  // this is for encoding user's password(using BCryptPasswordEncoder)(completed)
  @PatchMapping("/modify_for_admin")
  @Transactional
  public List<UserEntity> modify() {
    List<UserEntity> target = userRepository.findAll();

    target.forEach(each->{
      each.setUserPassword(bCryptPasswordEncoder.encode(each.getUserPassword()));
      userRepository.save(each);
    });

    return userRepository.findAll();
  }

}

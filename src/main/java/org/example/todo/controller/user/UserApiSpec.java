package org.example.todo.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.todo.dto.user.UserAddRequest;
import org.example.todo.dto.user.UserRequest;
import org.example.todo.dto.user.UserUpdateRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "User Api", description = "기본적인 user CRUD 기능을 구현한 Api")
public interface UserApiSpec {

  @Operation(
      summary = "user 목록 조회",
      description = "모든 user를 List형태로 조회한다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "목록 조회 성공")
  })
  ResponseEntity<?> findAllUsers();

  @Operation(
      summary = "user 조회",
      description = "user id를 통해 특정 user를 조회한다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "회원 찾을 수 없음")
  })
  ResponseEntity<?> findUserById(
      @Parameter(description = "조회하고자 하는 user id")
      Long user_id);

  @Operation(
      summary = "user 등록",
      description = "회원가입 기능과는 별도, 이제 사용하지 않는 user 등록 api"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 완료")
  })
  ResponseEntity<?> addUser(
      @Parameter(description = "user name, email, password")
      UserRequest request);

  @Operation(
      summary = "user 정보 변경",
      description = "userName, email, password 정보 변경 api"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "변경 성공"),
      @ApiResponse(responseCode = "404", description = "user 찾을 수 없음")
  })
  ResponseEntity<?> updateUser(
      @Parameter(description = "변경하고자 하는 user id")
      Long user_id,
      @Parameter(description = "user name, email, password")
      UserRequest request);

  @Operation(
      summary = "user 삭제",
      description = "user id로 조회한 특정 user 정보 삭제"
  )
  @ApiResponses(value={
      @ApiResponse(responseCode = "200", description = "삭제 완료"),
      @ApiResponse(responseCode = "404", description = "user 찾을 수 없음")
  })
  ResponseEntity<?> deleteUser(
      @Parameter(description = "삭제하고자 하는 user id")
      Long user_id);
}

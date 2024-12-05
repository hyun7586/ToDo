package org.example.todo.controller.schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.todo.dto.schedule.ScheduleAddRequest;
import org.example.todo.dto.schedule.ScheduleUpdateRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

@Tag(name="Schedule Api", description = "기본적인 Schedule CRUD Api 구현")
public interface ScheduleApiSpec {

  @Operation(
      summary="일정 목록 조회",
      description = "모든 schedule 목록 조회"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "목록 조회 성공")
  })
  ResponseEntity<?> getAllSchedule();


  @Operation(
      summary = "특정 일정 조회",
      description = "schedule id를 통한 특정 schedule 정보 조회"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 schedule 존재하지 않음")
  })
  ResponseEntity<?> getSchedule(
      @Parameter(description = "조회하고자 하는 schedule id")
      Long schedule_id);

  @Operation(
      summary = "새로운 일정 생성",
      description = "새로운 schedule 추가"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공")
  })
  ResponseEntity<?> addSchedule(
      @Parameter(description = "일정 name, content, startedAt, endedAt, category")
      ScheduleAddRequest request);

  @Operation(
      summary = "특정 일정 정보 수정",
      description = "schedule id로 조회한 특정 일정의 정보 수정"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 완료"),
      @ApiResponse(responseCode = "404", description = "해당 일정 찾을 수 없음")
  })
  ResponseEntity<?> updateSchedule(
      @Parameter(description = "일정 id")
      Long schedule_id,
      @Parameter(description = "일정 name, content, startedAt, endedAt, category 중 수정사항")
      ScheduleUpdateRequest request);

  @Operation(
      summary = "특정 일정 완료 처리",
      description = "일정 id로 조회한 특정 일정을 완료 처리하는 api"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "처리 완료"),
      @ApiResponse(responseCode = "404", description = "해당 일정 찾을 수 없음")
  })
  ResponseEntity<?> completeSchedule(
      @Parameter(description = "일정 id")
      Long schedule_id);


  @Operation(
      summary = "특정 일정 삭제",
      description = "일정 id로 조회한 특정 일정 삭제"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 일정 찾을 수 없음")
  })
  ResponseEntity<?> deleteSchedule(
      @Parameter(description = "일정 id")
      Long schedule_id);
}

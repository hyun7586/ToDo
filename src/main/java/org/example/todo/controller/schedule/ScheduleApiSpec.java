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

  @Operation(
      summary = "특정 기준에 따른 일정 조회",
      description = "parameter로 전달된 일정 기준(month, week, day)에 따라 일정 조회"
  )
  @ApiResponses(value={
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "400", description = "level 입력값 잘못됨")
  })
  ResponseEntity<?> sortByDate(
      @Parameter(description = "조회 기준(month, week, day)")
      String level);


  @Operation(
      summary = "schedule 목록 page 조회 기능",
      description = "param으로 주어진 page, size에 따라 schedule 목록 조회"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공")
  })
  ResponseEntity<?> getSchedulePages(
      @Parameter(description = "조회할 page number(0부터 시작)") Integer page,
      @Parameter(description = "한 page에 들어갈 schedule 개수") Integer size
  );


}


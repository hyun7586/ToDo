package org.example.todo.controller.schedule;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.ScheduleEntity;
import org.example.todo.dto.mapper.ScheduleMapper;
import org.example.todo.dto.schedule.ScheduleRequest;
import org.example.todo.dto.schedule.ScheduleResponse;
import org.example.todo.service.schedule.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/api")
public class ScheduleApiController implements ScheduleApiSpec {

  private final ScheduleService scheduleService;
  private final ScheduleMapper scheduleMapper;

  // 모든 schedule 목록 조회
  @GetMapping("/all_schedules")
  public ResponseEntity<?> getAllSchedule() {
    List<ScheduleResponse> scheduleList = scheduleService.findAll().stream()
        .map(scheduleMapper::toResponse)
        .toList();

    return ResponseEntity.status(HttpStatus.OK)
        .body(scheduleList);
  }

  // 특정 일정 id로 조회
  @GetMapping("/schedule/{schedule_id}")
  public ResponseEntity<?> getSchedule(
      @PathVariable(name = "schedule_id") Long schedule_id
  ) {
    ScheduleEntity temp = scheduleService.findById(schedule_id);

    if (temp == null) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    } else {

      return ResponseEntity.status(HttpStatus.OK)
          .body(scheduleMapper.toResponse(temp));
    }
  }

  // 일정 추가
  @PostMapping("/schedule")
  public ResponseEntity<?> addSchedule(
      @RequestBody ScheduleRequest request
  ) {
    ScheduleEntity saved = scheduleService.save(scheduleMapper.toEntity(request));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(saved);
  }

  // 특정 일정정보 수정
  @PatchMapping("/schedule/{schedule_id}")
  public ResponseEntity<?> updateSchedule(
      @PathVariable(name = "schedule_id") Long schedule_id,
      @RequestBody ScheduleRequest request
  ) {
    ScheduleEntity target = scheduleService.findById(schedule_id);

    if (target == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    } else {
      // dto에 입력된 값만 수정
      if (request.getScheduleName() != null) {
        target.setScheduleName(request.getScheduleName());
      }
      if (request.getScheduleMemo() != null) {
        target.setScheduleMemo(request.getScheduleMemo());
      }
      if (request.getScheduleStartDate() != null) {
        target.setScheduleStartDate(request.getScheduleStartDate());
      }
      if (request.getScheduleEndDate() != null) {
        target.setScheduleEndDate(request.getScheduleEndDate());
      }
      if (request.getScheduleCategory() != null) {
        target.setScheduleCategory(request.getScheduleCategory());
      }

      ScheduleEntity updated = scheduleService.save(target);

      return ResponseEntity.status(HttpStatus.OK)
          .body(updated);
    }
  }

  // 특정 일정 완료 처리
  @PatchMapping("/schedule/complete/{schedule_id}")
  public ResponseEntity<?> completeSchedule(
      @PathVariable(name = "schedule_id") Long schedule_id
  ) {
    ScheduleEntity target = scheduleService.findById(schedule_id);

    if (target == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    }

    target.setScheduleStatus("DONE");

    return ResponseEntity.status(HttpStatus.OK)
        .body(target);
  }

  // 특정 일정 삭제
  @DeleteMapping("/schedule/{schedule_id}")
  public ResponseEntity<?> deleteSchedule(
      @PathVariable(name = "schedule_id") Long schedule_id
  ) {
    if (scheduleService.findById(schedule_id) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    }
    scheduleService.deleteById(schedule_id);
    return ResponseEntity.status(HttpStatus.OK)
        .body("the delete is completed");
  }

  // 날짜별 정렬 기능
  @GetMapping("/sort/{level}")
  public ResponseEntity<?> sortByDate(
      @PathVariable(name = "level") String level
  ) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime upperLimit = switch (level) {
      case "month" -> now.plusMonths(1);
      case "week" -> now.plusWeeks(1);
      case "day" -> now.plusDays(1);
      default -> null;
    };

    if (upperLimit == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid level: " + level);
    }

    List<ScheduleResponse> result = scheduleService.findAll().stream()
        .filter(each -> each.getScheduleStartDate().isAfter(now) &&
            each.getScheduleEndDate().isBefore(now) &&
            each.getScheduleEndDate().isBefore(upperLimit))
        .map(scheduleMapper::toResponse)
        .toList();

    return ResponseEntity.status(HttpStatus.OK)
        .body(result);
  }

  // paging
  @GetMapping("/pages")
  public ResponseEntity<?> getSchedulePages(
      @RequestParam Integer page,
      @RequestParam Integer size
  ) {
    Page<ScheduleEntity> temp = scheduleService.getSchedulePages(page, size);
    Page<ScheduleResponse> result = temp.map(scheduleMapper::toResponse);

    return ResponseEntity.status(HttpStatus.OK)
        .body(result);
  }
}

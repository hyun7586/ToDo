package org.example.todo.controller.schedule;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.ScheduleEntity;
import org.example.todo.dto.schedule.ScheduleAddRequest;
import org.example.todo.dto.schedule.ScheduleUpdateRequest;
import org.example.todo.dto.schedule.ScheduleViewResponse;
import org.example.todo.service.schedule.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/schedule/api")
public class ScheduleApiController implements ScheduleApiSpec{

  private final ScheduleService scheduleService;

  // 모든 schedule 목록 조회
  @GetMapping("/all_schedules")
  public ResponseEntity<?> getAllSchedule(){
    List<ScheduleEntity> scheduleList = scheduleService.findAll();

    return ResponseEntity.status(HttpStatus.OK)
        .body(scheduleList);
  }

  // 특정 일정 id로 조회
  @GetMapping("/schedule/{schedule_id}")
  public ResponseEntity<?> getSchedule(
      @PathVariable(name = "schedule_id") Long schedule_id
  ){
    ScheduleEntity temp = scheduleService.findById(schedule_id);

    if(temp==null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    } else {
      ScheduleViewResponse result = ScheduleViewResponse.builder()
          .scheduleId(temp.getScheduleId())
          .scheduleName(temp.getScheduleName())
          .scheduleMemo(temp.getScheduleMemo())
          .scheduleStartDate(temp.getScheduleStartDate())
          .scheduleEndDate(temp.getScheduleEndDate())
          .scheduleCategory(temp.getScheduleCategory())
          .build();

      return ResponseEntity.status(HttpStatus.OK)
          .body(result);
    }
  }

  // 일정 추가
  @PostMapping("/schedule")
  public ResponseEntity<?> addSchedule(
      @RequestBody ScheduleAddRequest request
  ){
    ScheduleEntity temp = ScheduleEntity.builder()
        .scheduleName(request.getScheduleName())
        .scheduleMemo(request.getScheduleMemo())
        .scheduleStartDate(request.getScheduleStartDate())
        .scheduleEndDate(request.getScheduleEndDate())
        .scheduleCategory(request.getScheduleCategory())
        .scheduleStatus("Not Started")
        .build();
    

    ScheduleEntity result = scheduleService.save(temp);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  // 특정 일정정보 수정
  @PatchMapping("/schedule/{schedule_id}")
  public ResponseEntity<?> updateSchedule(
      @PathVariable(name="schedule_id") Long schedule_id,
      @RequestBody ScheduleUpdateRequest request
  ){
    ScheduleEntity target = scheduleService.findById(schedule_id);

    if(target==null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    }else{
      // dto에 입력된 값만 수정
      if(request.getScheduleName()!=null){
        target.setScheduleName(request.getScheduleName());
      }
      if(request.getScheduleMemo()!=null){
        target.setScheduleMemo(request.getScheduleMemo());
      }
      if(request.getScheduleStartDate()!=null){
        target.setScheduleStartDate(request.getScheduleStartDate());
      }
      if(request.getScheduleEndDate()!=null){
        target.setScheduleEndDate(request.getScheduleEndDate());
      }
      if(request.getScheduleCategory()!=null){
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
      @PathVariable(name="schedule_id") Long schedule_id
  ){
    ScheduleEntity target = scheduleService.findById(schedule_id);

    if(target==null){
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
      @PathVariable(name="schedule_id") Long schedule_id
  ){
    if(scheduleService.findById(schedule_id)==null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("the schedule is not found");
    }
    scheduleService.deleteById(schedule_id);
    return ResponseEntity.status(HttpStatus.OK)
        .body("the delete is completed");
  }
}

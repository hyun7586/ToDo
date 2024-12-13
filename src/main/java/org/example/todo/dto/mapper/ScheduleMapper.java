package org.example.todo.dto.mapper;

import org.example.todo.domain.ScheduleEntity;
import org.example.todo.dto.schedule.ScheduleRequest;
import org.example.todo.dto.schedule.ScheduleResponse;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {
    public ScheduleResponse toResponse(ScheduleEntity entity){
      return ScheduleResponse.builder()
          .scheduleId(entity.getScheduleId())
          .scheduleName(entity.getScheduleName())
          .scheduleMemo(entity.getScheduleMemo())
          .scheduleStartDate(entity.getScheduleStartDate())
          .scheduleEndDate(entity.getScheduleEndDate())
          .scheduleCategory(entity.getScheduleCategory())
          .status(entity.getScheduleStatus())
          .build();
    }

    public ScheduleEntity toEntity(ScheduleRequest request){
      return ScheduleEntity.builder()
          .scheduleName(request.getScheduleName())
          .scheduleMemo(request.getScheduleMemo())
          .scheduleStartDate(request.getScheduleStartDate())
          .scheduleEndDate(request.getScheduleEndDate())
          .scheduleCategory(request.getScheduleCategory())
          .scheduleStatus("Not Started")
          .build();
    }
}

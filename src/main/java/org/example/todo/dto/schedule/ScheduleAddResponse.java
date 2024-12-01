package org.example.todo.dto.schedule;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleAddResponse {
  private Long scheduleId;
  private String scheduleName;
  private String scheduleMemo;

  private LocalDateTime scheduleStartDate;;
  private LocalDateTime scheduleEndDate;

  private String status;
  private String scheduleCategory;

}

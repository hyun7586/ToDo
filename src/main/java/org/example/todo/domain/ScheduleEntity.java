package org.example.todo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="schedule")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="schedule_id")
  private Long scheduleId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name="schedule_name")
  private String scheduleName;

  @Column(name="schedule_memo")
  private String scheduleMemo;

  @Column(name="schedule_start_date")
  private LocalDateTime scheduleStartDate;

  @Column(name="schedule_end_date")
  private LocalDateTime scheduleEndDate;

  @Column(name="schedule_status")
  private String scheduleStatus;

  @Column(name="schedule_category")
  private String scheduleCategory;
}

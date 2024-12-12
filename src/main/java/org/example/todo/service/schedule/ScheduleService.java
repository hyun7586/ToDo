package org.example.todo.service.schedule;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.ScheduleEntity;
import org.example.todo.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;

  // find the particular schedule
  public ScheduleEntity findById(Long id) {
    return scheduleRepository.findById(id).orElse(null);
  }

  // create/save the new one
  public ScheduleEntity save(ScheduleEntity entity) {
    return scheduleRepository.save(entity);
  }

  // find all schedules
  public List<ScheduleEntity> findAll() {
    return scheduleRepository.findAll();
  }

  // delete the schedule
  public void deleteById(Long id) {
    scheduleRepository.deleteById(id);
  }

  public Page<ScheduleEntity> getSchedulePages(int page, int size){
    Pageable pageable = PageRequest.of(page, size);
    return scheduleRepository.findAll(pageable);
  }
}

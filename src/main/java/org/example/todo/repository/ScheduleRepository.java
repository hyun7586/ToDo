package org.example.todo.repository;

import org.example.todo.domain.ScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

  Page<ScheduleEntity> findAll(Pageable pageable);
}

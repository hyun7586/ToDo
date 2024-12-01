package org.example.todo.repository;

import java.util.Optional;
import org.example.todo.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUserEmail(String email);
}

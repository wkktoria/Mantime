package io.github.wkktoria.managetime.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable pageable);

    Optional<Task> findById(Long id);

    boolean existsById(Long id);

    boolean existsByDoneIsFalseAndGroupId(Long groupId);

    Task save(Task task);

    List<Task> findByDone(@Param("status") boolean done);

    List<Task> findAllByGroupId(Long groupId);
}

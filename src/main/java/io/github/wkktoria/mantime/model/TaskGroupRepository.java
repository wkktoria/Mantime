package io.github.wkktoria.mantime.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Long id);

    TaskGroup save(TaskGroup taskGroup);

    boolean existsByDoneIsFalseAndProjectId(Long projectId);

    boolean existsByDescription(String description);
}

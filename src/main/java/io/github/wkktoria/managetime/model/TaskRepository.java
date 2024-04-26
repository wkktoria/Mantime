package io.github.wkktoria.managetime.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @RestResource(path = "done", rel = "done")
    List<Task> findByDone(@Param("status") boolean done);
}

package io.github.wkktoria.managetime.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
interface TaskRepository extends JpaRepository<Task, Long> {
    @RestResource(path = "done", rel = "done")
    List<Task> findByDone(@Param("status") boolean done);
}

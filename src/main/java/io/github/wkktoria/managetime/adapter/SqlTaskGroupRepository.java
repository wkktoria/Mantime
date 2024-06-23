package io.github.wkktoria.managetime.adapter;

import io.github.wkktoria.managetime.model.TaskGroup;
import io.github.wkktoria.managetime.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Long> {
    @Override
    @Query("select distinct tg from TaskGroup tg join fetch tg.tasks")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProjectId(Long projectId);
}

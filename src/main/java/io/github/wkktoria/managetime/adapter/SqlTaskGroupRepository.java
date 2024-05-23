package io.github.wkktoria.managetime.adapter;

import io.github.wkktoria.managetime.model.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    @Override
    @Query("select distinct  tg from TaskGroup tg join fetch tg.tasks")
    List<TaskGroup> findAll();
}

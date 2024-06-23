package io.github.wkktoria.managetime.adapter;

import io.github.wkktoria.managetime.model.Task;
import io.github.wkktoria.managetime.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
interface SqlTaskRepository extends JpaRepository<Task, Long>, TaskRepository {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from Tasks where id=:id")
    boolean existsById(@Param("id") @NonNull Long id);

    @Override
    boolean existsByDoneIsFalseAndGroupId(Long groupId);

    @Override
    List<Task> findAllByGroupId(Long groupId);

    @Override
    List<Task> findAllByDoneIsFalseAndDeadlineLessThanEqual(LocalDateTime deadline);
}

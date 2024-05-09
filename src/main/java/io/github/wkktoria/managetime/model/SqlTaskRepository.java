package io.github.wkktoria.managetime.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
interface SqlTaskRepository extends JpaRepository<Task, Long>, TaskRepository {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") @NonNull Long id);
}

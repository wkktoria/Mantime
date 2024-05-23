package io.github.wkktoria.managetime.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Long> {
}

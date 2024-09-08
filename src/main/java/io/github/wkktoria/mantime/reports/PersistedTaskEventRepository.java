package io.github.wkktoria.mantime.reports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent, Long> {
    List<PersistedTaskEvent> findByTaskId(Long taskId);
}

package io.github.wkktoria.mantime.reports;

import io.github.wkktoria.mantime.model.event.TaskEvent;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "task_events")
class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long taskId;
    LocalDateTime occurrence;
    String name;

    public PersistedTaskEvent() {
    }

    PersistedTaskEvent(final TaskEvent source) {
        taskId = source.getTaskId();
        occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
        name = source.getClass().getSimpleName();
    }
}

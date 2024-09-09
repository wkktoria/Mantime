package io.github.wkktoria.mantime.reports;

import io.github.wkktoria.mantime.model.Task;
import io.github.wkktoria.mantime.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository eventRepository;

    ReportController(final TaskRepository taskRepository, final PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithChangesCount(@PathVariable final Long id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doneBeforeDeadline/{id}")
    ResponseEntity<TaskWithDoneBeforeDeadline> readTaskWithDoneBeforeDeadline(@PathVariable final Long id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithDoneBeforeDeadline(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;

        TaskWithChangesCount(final Task task, final List<PersistedTaskEvent> event) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = event.size();
        }
    }

    private static class TaskWithDoneBeforeDeadline {
        public String description;
        public LocalDateTime deadline;
        public LocalDateTime occurrence;
        public boolean done;
        public boolean doneBeforeDeadline;

        TaskWithDoneBeforeDeadline(final Task task, final List<PersistedTaskEvent> event) {
            description = task.getDescription();
            deadline = task.getDeadline();
            occurrence = event.getLast().occurrence;
            done = task.isDone();

            if (deadline == null) {
                doneBeforeDeadline = true;
            } else if (done) {
                doneBeforeDeadline = !event.isEmpty() && occurrence.isBefore(deadline);
            } else {
                doneBeforeDeadline = false;
            }
        }
    }
}

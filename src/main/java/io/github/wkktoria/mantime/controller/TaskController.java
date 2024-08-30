package io.github.wkktoria.mantime.controller;

import io.github.wkktoria.mantime.model.Task;
import io.github.wkktoria.mantime.model.TaskRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    TaskController(final TaskRepository repository, final ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.info("Reading all the tasks");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
        logger.info("Reading all the pageable tasks");
        return ResponseEntity.ok(repository.findAll(pageable).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable Long id) {
        logger.info("Reading task with id {}", id);
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repository.findById(id).get());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true", required = false) boolean status) {
        logger.info("Reading all done tasks");
        return ResponseEntity.ok(repository.findByDone(status));
    }

    @GetMapping("/search/today")
    ResponseEntity<List<Task>> readTodayTasks() {
        logger.info("Reading all today tasks");
        return ResponseEntity.ok(repository.findAllByDoneIsFalseAndDeadlineLessThanEqual(LocalDateTime.now()));
    }

    @PostMapping
    ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        logger.info("Creating new task");
        Task result = repository.save(task);
        return ResponseEntity.created(URI.create("/tasks/" + result.getId())).body(result);
    }

    @Transactional
    @PutMapping("/{id}")
    ResponseEntity<Void> updateTask(@PathVariable Long id, @Valid @RequestBody Task toUpdate) {
        logger.info("Updating task with id {}", id);
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.updateFrom(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Void> toggleTask(@PathVariable Long id) {
        logger.info("Toggling task with id {}", id);
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(eventPublisher::publishEvent);
        return ResponseEntity.noContent().build();
    }
}

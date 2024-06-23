package io.github.wkktoria.mantime.controller;

import io.github.wkktoria.mantime.logic.TaskGroupService;
import io.github.wkktoria.mantime.model.Task;
import io.github.wkktoria.mantime.model.TaskRepository;
import io.github.wkktoria.mantime.model.projection.GroupReadModel;
import io.github.wkktoria.mantime.model.projection.GroupWriteModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService service;
    private final TaskRepository repository;

    TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Reading all the groups");
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable Long id) {
        logger.info("Reading all the tasks from the group with id {}", id);
        return ResponseEntity.ok(repository.findAllByGroupId(id));
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@Valid @RequestBody GroupWriteModel model) {
        logger.info("Creating new group");
        GroupReadModel group = service.createGroup(model);
        return ResponseEntity.created(URI.create("/" + group.getId())).body(group);
    }

    @Transactional
    @PatchMapping("/{id}")
    ResponseEntity<Void> toggleGroup(@PathVariable Long id) {
        logger.info("Toggle group with id {}", id);
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

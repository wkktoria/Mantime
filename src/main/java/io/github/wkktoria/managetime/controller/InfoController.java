package io.github.wkktoria.managetime.controller;

import io.github.wkktoria.managetime.TaskConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class InfoController {
    private final TaskConfigurationProperties taskConfigurationProperties;

    InfoController(final TaskConfigurationProperties taskConfigurationProperties) {
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    @GetMapping("info/allowMultipleTasks")
    boolean isAllowMultipleTasks() {
        return taskConfigurationProperties.isAllowMultipleTasksFromTemplate();
    }
}

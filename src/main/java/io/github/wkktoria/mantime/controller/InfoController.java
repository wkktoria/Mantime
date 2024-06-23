package io.github.wkktoria.mantime.controller;

import io.github.wkktoria.mantime.TaskConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
class InfoController {
    private final TaskConfigurationProperties taskConfigurationProperties;

    InfoController(final TaskConfigurationProperties taskConfigurationProperties) {
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    @GetMapping("/allowMultipleTasks")
    boolean isAllowMultipleTasks() {
        return taskConfigurationProperties.getTemplate().isAllowMultipleTasks();
    }
}

package io.github.wkktoria.managetime.logic;

import io.github.wkktoria.managetime.TaskConfigurationProperties;
import io.github.wkktoria.managetime.model.ProjectRepository;
import io.github.wkktoria.managetime.model.TaskGroupRepository;
import io.github.wkktoria.managetime.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(final ProjectRepository repository,
                                  final TaskGroupRepository taskGroupRepository,
                                  final TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        return new TaskGroupService(repository, taskRepository);
    }
}

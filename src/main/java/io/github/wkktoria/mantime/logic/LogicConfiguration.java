package io.github.wkktoria.mantime.logic;

import io.github.wkktoria.mantime.TaskConfigurationProperties;
import io.github.wkktoria.mantime.model.ProjectRepository;
import io.github.wkktoria.mantime.model.TaskGroupRepository;
import io.github.wkktoria.mantime.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(final ProjectRepository repository,
                                  final TaskGroupRepository taskGroupRepository,
                                  final TaskConfigurationProperties config,
                                  final TaskGroupService taskGroupService) {
        return new ProjectService(repository, taskGroupRepository, config, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        return new TaskGroupService(repository, taskRepository);
    }
}

package io.github.wkktoria.managetime.logic;

import io.github.wkktoria.managetime.TaskConfigurationProperties;
import io.github.wkktoria.managetime.model.ProjectRepository;
import io.github.wkktoria.managetime.model.TaskGroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService service(final ProjectRepository repository,
                           final TaskGroupRepository taskGroupRepository,
                           final TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config);
    }
}

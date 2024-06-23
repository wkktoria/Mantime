package io.github.wkktoria.mantime.logic;

import io.github.wkktoria.mantime.TaskConfigurationProperties;
import io.github.wkktoria.mantime.model.Project;
import io.github.wkktoria.mantime.model.ProjectRepository;
import io.github.wkktoria.mantime.model.TaskGroupRepository;
import io.github.wkktoria.mantime.model.projection.GroupReadModel;
import io.github.wkktoria.mantime.model.projection.GroupTaskWriteModel;
import io.github.wkktoria.mantime.model.projection.GroupWriteModel;
import io.github.wkktoria.mantime.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties config;
    private final TaskGroupService taskGroupService;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository,
                          final TaskConfigurationProperties config, final TaskGroupService taskGroupService) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(ProjectWriteModel toSave) {
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, Long projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProjectId(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        GroupReadModel result = repository.findById(projectId)
                .map(project -> {
                    GroupWriteModel targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(step -> {
                                GroupTaskWriteModel task = new GroupTaskWriteModel();
                                task.setDescription(step.getDescription());
                                task.setDeadline(deadline.plusDays(step.getDaysToDeadline()));
                                return task;
                            }).collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return result;
    }
}

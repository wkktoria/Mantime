package io.github.wkktoria.mantime.logic;

import io.github.wkktoria.mantime.model.Project;
import io.github.wkktoria.mantime.model.TaskGroup;
import io.github.wkktoria.mantime.model.TaskGroupRepository;
import io.github.wkktoria.mantime.model.TaskRepository;
import io.github.wkktoria.mantime.model.projection.GroupReadModel;
import io.github.wkktoria.mantime.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(Long groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}

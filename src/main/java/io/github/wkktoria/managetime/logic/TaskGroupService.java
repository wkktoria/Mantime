package io.github.wkktoria.managetime.logic;

import io.github.wkktoria.managetime.model.TaskGroup;
import io.github.wkktoria.managetime.model.TaskGroupRepository;
import io.github.wkktoria.managetime.model.TaskRepository;
import io.github.wkktoria.managetime.model.projection.GroupReadModel;
import io.github.wkktoria.managetime.model.projection.GroupWriteModel;

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
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(Long groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}

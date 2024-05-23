package io.github.wkktoria.managetime.model.projection;

import io.github.wkktoria.managetime.model.Task;
import io.github.wkktoria.managetime.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private String description;
    private LocalDateTime deadline; // Deadline of the latest task in group.
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup group) {
        description = group.getDescription();
        group.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo).ifPresent(deadline -> this.deadline = deadline);
        tasks = group.getTasks().stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}

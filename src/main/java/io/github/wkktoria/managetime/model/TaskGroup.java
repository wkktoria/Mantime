package io.github.wkktoria.managetime.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Task group description cannot be empty")
    private String description;
    private boolean done;
    @Embedded
    private Audit audit = new Audit();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public TaskGroup() {
    }

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    void setDone(final boolean done) {
        this.done = done;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    Project getProject() {
        return project;
    }

    void setProject(final Project project) {
        this.project = project;
    }
}

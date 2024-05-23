package io.github.wkktoria.managetime.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Project description cannot be empty")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;
    @OneToMany(mappedBy = "project")
    private Set<ProjectStep> steps;

    public Project() {
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

    void setDescription(final String description) {
        this.description = description;
    }

    Set<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(final Set<TaskGroup> groups) {
        this.groups = groups;
    }

    Set<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(final Set<ProjectStep> steps) {
        this.steps = steps;
    }
}

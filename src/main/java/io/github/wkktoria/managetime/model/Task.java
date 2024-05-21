package io.github.wkktoria.managetime.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Task description cannot be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public Task() {
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

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean completed) {
        this.done = completed;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateFrom(Task source) {
        description = source.getDescription();
        done = source.isDone();
        deadline = source.getDeadline();
    }

    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedOn = LocalDateTime.now();
    }
}

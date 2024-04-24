package io.github.wkktoria.managetime.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private boolean done;

    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    boolean isDone() {
        return done;
    }

    void setDone(boolean completed) {
        this.done = completed;
    }
}

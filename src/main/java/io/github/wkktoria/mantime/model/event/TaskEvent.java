package io.github.wkktoria.mantime.model.event;

import io.github.wkktoria.mantime.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    private Long taskId;
    private Instant occurrence;
    
    TaskEvent(final long taskId, final Clock clock) {
        this.taskId = taskId;
        occurrence = Instant.now(clock);
    }

    public static TaskEvent changed(final Task source) {
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    public long getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}

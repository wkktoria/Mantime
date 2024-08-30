package io.github.wkktoria.mantime.model.event;

import io.github.wkktoria.mantime.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}

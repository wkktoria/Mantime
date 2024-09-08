package io.github.wkktoria.mantime.reports;

import io.github.wkktoria.mantime.model.event.TaskDone;
import io.github.wkktoria.mantime.model.event.TaskEvent;
import io.github.wkktoria.mantime.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    ChangedTaskEventListener(final PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(TaskDone event) {
        onChanged(event);
    }

    @EventListener
    public void on(TaskUndone event) {
        onChanged(event);
    }

    private void onChanged(final TaskEvent event) {
        logger.info("Got task event: {}", event);
        repository.save(new PersistedTaskEvent(event));
    }
}

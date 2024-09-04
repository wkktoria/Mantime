package io.github.wkktoria.mantime.reports;

import io.github.wkktoria.mantime.model.event.TaskDone;
import io.github.wkktoria.mantime.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    @EventListener
    public void on(TaskDone event) {
        logger.info("Got task done event: {}", event);
    }

    public void on(TaskUndone event) {
        logger.info("Got task undone event: {}", event);
    }
}

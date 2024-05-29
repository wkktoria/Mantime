package io.github.wkktoria.managetime;

import io.github.wkktoria.managetime.model.Task;
import io.github.wkktoria.managetime.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Configuration
class TestConfiguration {
    @Bean
    @Profile({"integration", "!prod"})
    TaskRepository testRepository() {
        return new TaskRepository() {
            private Map<Long, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll(final Pageable pageable) {
                return null;
            }

            @Override
            public Optional<Task> findById(final Long id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public boolean existsById(final Long id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroupId(final Long groupId) {
                return false;
            }

            @Override
            public Task save(final Task task) {
                return tasks.put((long) (tasks.size() + 1), task);
            }

            @Override
            public List<Task> findByDone(final boolean done) {
                return List.of();
            }
        };
    }
}

package io.github.wkktoria.managetime;

import io.github.wkktoria.managetime.model.Task;
import io.github.wkktoria.managetime.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
class TestConfiguration {
    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile({"integration", "!prod"})
    TaskRepository testRepository() {
        return new TaskRepository() {
            private final Map<Long, Task> tasks = new HashMap<>();

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
                long key = tasks.size() + 1;
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(task, key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                tasks.put(key, task);
                return tasks.get(key);
            }

            @Override
            public List<Task> findByDone(final boolean done) {
                return List.of();
            }

            @Override
            public List<Task> findAllByGroupId(final Long groupId) {
                return List.of();
            }

            @Override
            public List<Task> findAllByDoneIsFalseAndDeadlineLessThanEqual(final LocalDateTime deadline) {
                return List.of();
            }
        };
    }
}

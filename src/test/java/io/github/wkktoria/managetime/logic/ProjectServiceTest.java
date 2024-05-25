package io.github.wkktoria.managetime.logic;

import io.github.wkktoria.managetime.TaskConfigurationProperties;
import io.github.wkktoria.managetime.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {
    private static TaskConfigurationProperties configurationReturning(final boolean b) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        return mockConfig;
    }

    private static TaskGroupRepository groupRepositoryReturning(final boolean b) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyLong())).thenReturn(b);

        return mockGroupRepository;
    }

    private static InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        var steps = daysToDeadline.stream()
                .map(dayToDeadline -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(dayToDeadline);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);

        String description = result.getDescription();

        return result;
    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        // given
        var mockGroupRepository = groupRepositoryReturning(true);
        var mockConfig = configurationReturning(false);

        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0L));

        // then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration is ok and no projects for given id")
    void createGroup_configurationIsOk_And_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());

        var mockConfig = configurationReturning(true);

        // system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0L));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just one group and no groups and no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());

        var mockGroupRepository = groupRepositoryReturning(false);
        var mockConfig = configurationReturning(true);

        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0L));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from the project")
    void createGroup_configurationIsOk_existingProject_createsAndSavesGroup() {
        // given
        var project = projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(project));

        var mockConfig = configurationReturning(true);
        var inMemoryGroupRepository = inMemoryGroupRepository();

        var today = LocalDate.now().atStartOfDay();
        int countBeforeCall = inMemoryGroupRepository.count();

        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, mockConfig);

        // when
        var result = toTest.createGroup(today, 1L);

        // then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks().stream().allMatch(task -> task.getDescription().equals("foo")));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private final Map<Integer, TaskGroup> map = new HashMap<>();
        private int index = 0;

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Long id) {
            return Optional.ofNullable(map.get(Math.toIntExact(id)));
        }

        @Override
        public TaskGroup save(final TaskGroup taskGroup) {
            if (taskGroup.getId() == null) {
                try {
                    var field = taskGroup.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    index += 1;
                    field.set(taskGroup, (long) index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(Math.toIntExact(taskGroup.getId()), taskGroup);

            return taskGroup;
        }

        @Override
        public boolean existsByDoneIsFalseAndProjectId(final Long projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && Objects.equals(group.getProject().getId(), projectId));
        }
    }
}
package io.github.wkktoria.mantime.logic;

import io.github.wkktoria.mantime.model.TaskGroup;
import io.github.wkktoria.mantime.model.TaskGroupRepository;
import io.github.wkktoria.mantime.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    private static TaskRepository taskRepositoryReturning(final boolean b) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyLong())).thenReturn(b);

        return mockTaskRepository;
    }

    @Test
    @DisplayName("should throw IllegalStateException when undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
        // given
        var mockTaskRepository = taskRepositoryReturning(true);

        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1L));

        // then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when done tasks but no group")
    void toggleGroup_wrongGroupId_throwsIllegalArgumentException() {
        // given
        var mockTaskRepository = taskRepositoryReturning(false);
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        // system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1L));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_worksAsExpected() {
        // given
        var mockTaskRepository = taskRepositoryReturning(false);
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyLong())).thenReturn(Optional.of(group));

        // system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        // when
        toTest.toggleGroup(1L);

        // then
        assertThat(group.isDone()).isNotEqualTo(beforeToggle);
    }
}
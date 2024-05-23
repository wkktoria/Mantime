package io.github.wkktoria.managetime.logic;

import io.github.wkktoria.managetime.TaskConfigurationProperties;
import io.github.wkktoria.managetime.model.ProjectRepository;
import io.github.wkktoria.managetime.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {
    private static TaskConfigurationProperties configurationReturning(final boolean t) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(t);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        return mockConfig;
    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        // given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyLong())).thenReturn(true);

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
}
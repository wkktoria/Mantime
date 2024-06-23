package io.github.wkktoria.mantime.controller;

import io.github.wkktoria.mantime.model.Task;
import io.github.wkktoria.mantime.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TaskController.class)
class TaskControllerLightIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskRepository repository;

    @Test
    void httpFet_returnsGivenTask() throws Exception {
        // given
        String description = "foo";
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1"))
                .andExpect(content().string(containsString(description)));
    }
}
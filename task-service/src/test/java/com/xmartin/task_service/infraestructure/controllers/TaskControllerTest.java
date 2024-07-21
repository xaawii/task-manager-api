package com.xmartin.task_service.infraestructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.task_service.application.service.TaskService;
import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.controllers.dto.CreateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.dto.TaskResponse;
import com.xmartin.task_service.infraestructure.controllers.dto.UpdateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.mappers.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    private TaskModel taskModel;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        LocalDateTime now = LocalDateTime.now();

        taskModel = TaskModel.builder()
                .id(1L)
                .title("Sample Task")
                .description("This is a sample task")
                .createDate(now)
                .updateDate(now)
                .dueDate(now.plusDays(7))
                .status(Status.PENDING)
                .userId(1)
                .build();

        taskResponse = TaskResponse.builder()
                .id(1L)
                .title("Sample Task")
                .description("This is a sample task")
                .createDate(now)
                .updateDate(now)
                .dueDate(now.plusDays(7))
                .status(Status.PENDING)
                .userId(1)
                .build();
    }

    @Test
    void testGetTaskById_success() throws Exception {
        //Given
        Long id = 1L;

        when(taskService.getTaskById(id)).thenReturn(taskModel);
        when(taskMapper.toResponse(taskModel)).thenReturn(taskResponse);


        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/task/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetTaskById_notFound() throws Exception {
        //Given
        Long id = 1L;

        when(taskService.getTaskById(id)).thenThrow(new TaskNotFoundException());

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/task/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTasksByUserId_success() throws Exception {
        //Given
        List<TaskModel> taskModels = Collections.singletonList(taskModel);
        List<TaskResponse> taskResponses = Collections.singletonList(taskResponse);

        when(taskService.getTasksByUserId(1L)).thenReturn(taskModels);
        when(taskMapper.toResponseList(taskModels)).thenReturn(taskResponses);

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/task/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    void testCreateTask_success() throws Exception {
        //Given
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();

        when(taskMapper.toModel(any(CreateTaskRequest.class), anyInt())).thenReturn(taskModel);
        when(taskService.createTask(any(TaskModel.class))).thenReturn(taskModel);
        when(taskMapper.toResponse(taskModel)).thenReturn(taskResponse);


        //When - Then
        mockMvc.perform(post("/task/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskResponse.getId()));
    }

    @Test
    void testUpdateTask_success() throws Exception {
        //Given
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();

        when(taskMapper.toModel(any(UpdateTaskRequest.class), anyLong())).thenReturn(taskModel);
        when(taskService.updateTask(any(TaskModel.class))).thenReturn(taskModel);
        when(taskMapper.toResponse(taskModel)).thenReturn(taskResponse);

        //When - Then
        mockMvc.perform(put("/task/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskResponse.getId()));
    }

    @Test
    void testUpdateTask_NotFound() throws Exception {
        // Given
        long id = 1L;
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();

        when(taskMapper.toModel(any(UpdateTaskRequest.class), anyLong())).thenReturn(taskModel);
        when(taskService.updateTask(any(TaskModel.class))).thenThrow(new TaskNotFoundException());

        //When - Then
        mockMvc.perform(put("/task/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void testDeleteTaskById_success() throws Exception {
        //Given
        Long id = 1L;

        //When - Then
        mockMvc.perform(delete("/task/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted."));
    }

    @Test
    void testDeleteTaskById_NotFound() throws Exception {
        // Given
        long id = 1L;

        doThrow(new TaskNotFoundException()).when(taskService).deleteTask(id);

        //When - Then
        mockMvc.perform(delete("/task/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }
}
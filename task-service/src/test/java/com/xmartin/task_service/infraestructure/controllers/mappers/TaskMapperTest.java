package com.xmartin.task_service.infraestructure.controllers.mappers;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.controllers.dto.CreateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.dto.TaskResponse;
import com.xmartin.task_service.infraestructure.controllers.dto.UpdateTaskRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {
    @InjectMocks
    private TaskMapper taskMapper;

    @Test
    void testToModel_CreateTaskRequest() {

        //Given
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("Test Task");
        createTaskRequest.setDescription("Description");
        createTaskRequest.setDueDate(LocalDateTime.now());
        createTaskRequest.setStatus(Status.PENDING);
        int userId = 1;

        //When
        TaskModel taskModel = taskMapper.toModel(createTaskRequest, userId);

        //Then
        assertEquals("Test Task", taskModel.getTitle());
        assertEquals("Description", taskModel.getDescription());
        assertEquals(createTaskRequest.getDueDate(), taskModel.getDueDate());
        assertEquals(Status.PENDING, taskModel.getStatus());
        assertEquals(userId, taskModel.getUserId());
    }

    @Test
    void testToModel_UpdateTaskRequest() {
        //Given
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle("Updated Task");
        updateTaskRequest.setDescription("Updated Description");
        updateTaskRequest.setDueDate(LocalDateTime.now().plusDays(1));
        updateTaskRequest.setStatus(Status.COMPLETED);
        long id = 1L;

        //When
        TaskModel taskModel = taskMapper.toModel(updateTaskRequest, id);

        //Then
        assertEquals(id, taskModel.getId());
        assertEquals("Updated Task", taskModel.getTitle());
        assertEquals("Updated Description", taskModel.getDescription());
        assertEquals(updateTaskRequest.getDueDate(), taskModel.getDueDate());
        assertEquals(Status.COMPLETED, taskModel.getStatus());
    }

    @Test
    void testToResponse() {

        //Given
        TaskModel taskModel = TaskModel.builder()
                .id(1L)
                .title("Test Task")
                .description("Description")
                .createDate(LocalDateTime.now().minusDays(1))
                .updateDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(1))
                .status(Status.PENDING)
                .userId(1)
                .build();

        //When
        TaskResponse taskResponse = taskMapper.toResponse(taskModel);

        //Then
        assertEquals(1L, taskResponse.getId());
        assertEquals("Test Task", taskResponse.getTitle());
        assertEquals("Description", taskResponse.getDescription());
        assertEquals(taskModel.getCreateDate(), taskResponse.getCreateDate());
        assertEquals(taskModel.getUpdateDate(), taskResponse.getUpdateDate());
        assertEquals(taskModel.getDueDate(), taskResponse.getDueDate());
        assertEquals(Status.PENDING, taskResponse.getStatus());
        assertEquals(1, taskResponse.getUserId());
    }

    @Test
    void testToResponseList() {

        //Given
        TaskModel taskModel1 = TaskModel.builder()
                .id(1L)
                .title("Task 1")
                .description("Description 1")
                .createDate(LocalDateTime.now().minusDays(1))
                .updateDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(1))
                .status(Status.PENDING)
                .userId(1)
                .build();

        TaskModel taskModel2 = TaskModel.builder()
                .id(2L)
                .title("Task 2")
                .description("Description 2")
                .createDate(LocalDateTime.now().minusDays(2))
                .updateDate(LocalDateTime.now().minusDays(1))
                .dueDate(LocalDateTime.now().plusDays(2))
                .status(Status.COMPLETED)
                .userId(2)
                .build();

        List<TaskModel> taskModelList = List.of(taskModel1, taskModel2);

        //When
        List<TaskResponse> taskResponseList = taskMapper.toResponseList(taskModelList);

        //Then
        assertEquals(2, taskResponseList.size());
        assertEquals(1L, taskResponseList.get(0).getId());
        assertEquals(2L, taskResponseList.get(1).getId());
        assertEquals(Status.PENDING, taskResponseList.get(0).getStatus());
        assertEquals(Status.COMPLETED, taskResponseList.get(1).getStatus());
    }

}
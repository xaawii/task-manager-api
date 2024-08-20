package com.xmartin.task_service.infraestructure.entity.converters;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.entity.TaskEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskConverterTest {
    @InjectMocks
    private TaskConverter taskConverter;

    @Test
    void testToModel() {

        //Given
        TaskEntity taskEntity = TaskEntity.builder()
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
        TaskModel taskModel = taskConverter.toModel(taskEntity);

        //Then
        assertEquals(1L, taskModel.getId());
        assertEquals("Test Task", taskModel.getTitle());
        assertEquals("Description", taskModel.getDescription());
        assertEquals(taskEntity.getCreateDate(), taskModel.getCreateDate());
        assertEquals(taskEntity.getUpdateDate(), taskModel.getUpdateDate());
        assertEquals(taskEntity.getDueDate(), taskModel.getDueDate());
        assertEquals(Status.PENDING, taskModel.getStatus());
        assertEquals(1, taskModel.getUserId());
    }

    @Test
    void testToEntity() {

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
        TaskEntity taskEntity = taskConverter.toEntity(taskModel);

        //Then
        assertEquals(1L, taskEntity.getId());
        assertEquals("Test Task", taskEntity.getTitle());
        assertEquals("Description", taskEntity.getDescription());
        assertEquals(taskModel.getCreateDate(), taskEntity.getCreateDate());
        assertEquals(taskModel.getUpdateDate(), taskEntity.getUpdateDate());
        assertEquals(taskModel.getDueDate(), taskEntity.getDueDate());
        assertEquals(Status.PENDING, taskEntity.getStatus());
        assertEquals(1, taskEntity.getUserId());
    }

}
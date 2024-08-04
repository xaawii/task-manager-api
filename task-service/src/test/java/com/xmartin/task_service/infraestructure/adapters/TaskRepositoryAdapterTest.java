package com.xmartin.task_service.infraestructure.adapters;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.entity.TaskEntity;
import com.xmartin.task_service.infraestructure.entity.converters.TaskConverter;
import com.xmartin.task_service.infraestructure.repository.JpaTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryAdapterTest {

    @InjectMocks
    private TaskRepositoryAdapter taskRepositoryAdapter;

    @Mock
    private JpaTaskRepository jpaTaskRepository;

    @Mock
    private TaskConverter taskConverter;


    private TaskModel taskModel;
    private TaskModel savedTaskModel;

    @BeforeEach
    void setUp() {
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

        savedTaskModel = TaskModel.builder()
                .id(1L)
                .title("Saved Task")
                .description("This is a saved task")
                .createDate(now)
                .updateDate(now)
                .dueDate(now.plusDays(7))
                .status(Status.COMPLETED)
                .userId(1)
                .build();
    }

    @Test
    void testDeleteTask_TaskExists() {
        //Given
        Long taskId = 1L;

        when(jpaTaskRepository.existsById(taskId)).thenReturn(true);

        //When
        assertDoesNotThrow(() -> taskRepositoryAdapter.deleteTask(taskId));

        //Then
        verify(jpaTaskRepository).existsById(taskId);
        verify(jpaTaskRepository).deleteById(taskId);
    }

    @Test
    void testDeleteTask_TaskDoesNotExist() {
        //Given
        Long taskId = 5L;

        when(jpaTaskRepository.existsById(taskId)).thenReturn(false);

        //When
        assertThrows(TaskNotFoundException.class, () -> taskRepositoryAdapter.deleteTask(taskId));

        //Then
        verify(jpaTaskRepository).existsById(taskId);
        verify(jpaTaskRepository, never()).deleteById(taskId);
    }

    @Test
    void testSaveTask() {
        //Given
        TaskEntity taskEntity = new TaskEntity();
        TaskEntity savedTaskEntity = new TaskEntity();

        when(taskConverter.toEntity(taskModel)).thenReturn(taskEntity);
        when(jpaTaskRepository.save(taskEntity)).thenReturn(savedTaskEntity);
        when(taskConverter.toModel(savedTaskEntity)).thenReturn(savedTaskModel);

        //When
        TaskModel result = taskRepositoryAdapter.saveTask(taskModel);

        //Then
        assertNotNull(result);
        assertEquals(savedTaskModel.getId(), result.getId());
        assertEquals(savedTaskModel.getTitle(), result.getTitle());

        verify(taskConverter).toEntity(taskModel);
        verify(jpaTaskRepository).save(taskEntity);
        verify(taskConverter).toModel(savedTaskEntity);
    }

    @Test
    void testGetTaskById_TaskExists() {
        //Given
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();

        when(jpaTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskConverter.toModel(taskEntity)).thenReturn(savedTaskModel);

        //When
        TaskModel result = assertDoesNotThrow(() -> taskRepositoryAdapter.getTaskById(taskId));

        //Then
        assertNotNull(result);
        assertEquals(savedTaskModel.getId(), result.getId());
        assertEquals(savedTaskModel.getTitle(), result.getTitle());

        verify(jpaTaskRepository).findById(taskId);
        verify(taskConverter).toModel(taskEntity);
    }

    @Test
    void testGetTaskById_TaskDoesNotExist() {
        //Given
        Long taskId = 5L;

        when(jpaTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        //When
        assertThrows(TaskNotFoundException.class, () -> taskRepositoryAdapter.getTaskById(taskId));

        //Then
        verify(jpaTaskRepository).findById(taskId);
        verify(taskConverter, never()).toModel(any(TaskEntity.class));
    }

    @Test
    void testGetTasksByUserId() {
        //Given
        Long userId = 1L;
        TaskEntity taskEntity = new TaskEntity();

        when(jpaTaskRepository.findTasksByUserId(userId)).thenReturn(Collections.singletonList(taskEntity));
        when(taskConverter.toModelList(anyList())).thenReturn(Collections.singletonList(savedTaskModel));

        //When

        List<TaskModel> result = taskRepositoryAdapter.getTasksByUserId(userId);

        //Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(savedTaskModel.getId(), result.get(0).getId());
        assertEquals(savedTaskModel.getTitle(), result.get(0).getTitle());

        verify(jpaTaskRepository).findTasksByUserId(userId);
        verify(taskConverter).toModelList(anyList());
    }

    @Test
    void testDeleteAllTasksByUserId() {
        //Given
        Integer userId = 1;

        //When

        taskRepositoryAdapter.deleteAllTasksByUserId(userId);

        //Then
        verify(jpaTaskRepository).deleteByUserId(userId);

    }

    @Test
    void testDeleteTasksByIdInBatch() {
        //Given
        List<Long> ids = Collections.singletonList(1L);

        //When
        taskRepositoryAdapter.deleteTasksByIdInBatch(ids);

        //Then
        verify(jpaTaskRepository).deleteAllByIdInBatch(ids);

    }

    @Test
    void testGetTasksByIdInBatch() {
        //Given
        List<Long> ids = Collections.singletonList(1L);
        List<TaskEntity> entityList = Collections.singletonList(new TaskEntity());
        List<TaskModel> taskModelList = Collections.singletonList(taskModel);

        when(jpaTaskRepository.findAllById(ids)).thenReturn(entityList);
        when(taskConverter.toModelList(anyList())).thenReturn(taskModelList);

        //When
        List<TaskModel> resultList = taskRepositoryAdapter.getTasksByIdInBatch(ids);

        //Then
        assertEquals(1L, resultList.get(0).getId());
        verify(jpaTaskRepository).findAllById(ids);
        verify(taskConverter).toModelList(anyList());

    }
}
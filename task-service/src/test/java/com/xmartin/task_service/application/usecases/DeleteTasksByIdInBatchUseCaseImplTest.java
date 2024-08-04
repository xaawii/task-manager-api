package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.exceptions.UserNotFoundException;
import com.xmartin.task_service.domain.model.DeleteTaskEvent;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.out.EventPublisherPort;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTasksByIdInBatchUseCaseImplTest {

    @InjectMocks
    private DeleteTasksByIdInBatchUseCaseImpl deleteTasksByIdInBatchUseCase;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @Mock
    private UserClientRepositoryPort userClientRepositoryPort;


    private TaskModel firstTaskModel;
    private TaskModel secondTaskModel;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        firstTaskModel = TaskModel.builder()
                .id(1L)
                .title("Sample Task")
                .description("This is a sample task")
                .createDate(now)
                .updateDate(now)
                .dueDate(now.plusDays(7))
                .status(Status.PENDING)
                .userId(1)
                .build();

        secondTaskModel = TaskModel.builder()
                .id(2L)
                .title("Sample Task")
                .description("This is a sample task")
                .createDate(now)
                .updateDate(now)
                .dueDate(now.plusDays(7))
                .status(Status.PENDING)
                .userId(1)
                .build();

        userModel = UserModel.builder()
                .id(1)
                .name("Xavi")
                .email("xavi@example.com")
                .role("ROLE_USER")
                .build();
    }

    @Test
    void deleteTaskByIdInBatchTest_canDelete() {
        //Given
        List<Long> ids = List.of(1L, 2L);

        List<TaskModel> taskModelList = List.of(firstTaskModel, secondTaskModel);

        when(taskRepositoryPort.getTasksByIdInBatch(ids)).thenReturn(taskModelList);
        when(userClientRepositoryPort.getUserById(taskModelList.get(0).getUserId())).thenReturn(userModel);

        //When
        assertDoesNotThrow(() -> deleteTasksByIdInBatchUseCase.deleteTaskByIdInBatch(ids));

        //Then
        verify(taskRepositoryPort).getTasksByIdInBatch(ids);
        verify(taskRepositoryPort).deleteTasksByIdInBatch(ids);
        verify(userClientRepositoryPort).getUserById(firstTaskModel.getUserId());


        ArgumentCaptor<DeleteTaskEvent> eventCaptor = ArgumentCaptor.forClass(DeleteTaskEvent.class);
        verify(eventPublisherPort, times(taskModelList.size())).publish(eventCaptor.capture());
        List<DeleteTaskEvent> publishedEvents = eventCaptor.getAllValues();

        for (int i = 0; i < taskModelList.size(); i++) {
            TaskModel savedTaskModel = taskModelList.get(i);
            DeleteTaskEvent publishedEvent = publishedEvents.get(i);

            assertNotNull(publishedEvent);
            assertEquals(savedTaskModel.getId(), publishedEvent.id());
            assertEquals(savedTaskModel.getTitle(), publishedEvent.title());
            assertEquals(savedTaskModel.getDescription(), publishedEvent.description());
            assertEquals(savedTaskModel.getStatus(), publishedEvent.status());
            assertEquals(userModel, publishedEvent.user());
        }

    }

    @Test
    void deleteTaskByIdInBatchTest_TaskNotFound() {
        //Given
        List<Long> ids = List.of(1L, 2L);
        when(taskRepositoryPort.getTasksByIdInBatch(ids)).thenReturn(Collections.emptyList());

        //When - Then
        assertThrows(TaskNotFoundException.class, () -> deleteTasksByIdInBatchUseCase.deleteTaskByIdInBatch(ids));

        verify(taskRepositoryPort).getTasksByIdInBatch(ids);
        verify(taskRepositoryPort, never()).deleteTasksByIdInBatch(ids);
        verify(userClientRepositoryPort, never()).getUserById(anyInt());

    }

    @Test
    void deleteTaskByIdInBatchTest_UserNotFound() {
        //Given
        List<Long> ids = List.of(1L, 2L);
        List<TaskModel> taskModelList = List.of(firstTaskModel, secondTaskModel);

        when(taskRepositoryPort.getTasksByIdInBatch(ids)).thenReturn(taskModelList);
        when(userClientRepositoryPort.getUserById(taskModelList.get(0).getUserId())).thenReturn(null);

        //When - Then
        assertThrows(UserNotFoundException.class, () -> deleteTasksByIdInBatchUseCase.deleteTaskByIdInBatch(ids));

        verify(taskRepositoryPort).getTasksByIdInBatch(ids);
        verify(userClientRepositoryPort).getUserById(anyInt());
        verify(taskRepositoryPort, never()).deleteTasksByIdInBatch(ids);


    }
}
package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.model.UpdateTaskEvent;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTaskUseCaseImplTest {

    @InjectMocks
    private UpdateTaskUseCaseImpl updateTaskUseCase;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @Mock
    private UserClientRepositoryPort userClientRepositoryPort;

    private TaskModel updatedTaskModel;
    private TaskModel savedTaskModel;
    private TaskModel oldBbddTaskModel;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        updatedTaskModel = TaskModel.builder()
                .id(1L)
                .title("Sample Task")
                .description("This is a sample task")
                .dueDate(now.plusDays(7))
                .status(Status.COMPLETED)
                .userId(1)
                .build();

        savedTaskModel = TaskModel.builder()
                .id(1L)
                .title("Sample Task")
                .description("This is a sample task")
                .createDate(now)
                .updateDate(now)
                .dueDate(now.plusDays(7))
                .status(Status.COMPLETED)
                .userId(1)
                .build();

        oldBbddTaskModel = TaskModel.builder()
                .id(1L)
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
    void testUpdateTask_canUpdate() throws TaskNotFoundException {

        //Given
        when(taskRepositoryPort.getTaskById(updatedTaskModel.getId())).thenReturn(oldBbddTaskModel);
        when(taskRepositoryPort.saveTask(updatedTaskModel)).thenReturn(savedTaskModel);
        when(userClientRepositoryPort.getUserById(savedTaskModel.getUserId())).thenReturn(userModel);

        //When
        TaskModel result = updateTaskUseCase.updateTask(updatedTaskModel);

        //Then
        assertNotNull(result);
        assertEquals(savedTaskModel.getId(), result.getId());
        assertEquals(savedTaskModel.getTitle(), result.getTitle());

        verify(taskRepositoryPort).getTaskById(updatedTaskModel.getId());
        verify(taskRepositoryPort).saveTask(any(TaskModel.class));
        verify(userClientRepositoryPort).getUserById(savedTaskModel.getUserId());

        ArgumentCaptor<UpdateTaskEvent> eventCaptor = ArgumentCaptor.forClass(UpdateTaskEvent.class);
        verify(eventPublisherPort).publish(eventCaptor.capture());
        UpdateTaskEvent publishedEvent = eventCaptor.getValue();

        assertNotNull(publishedEvent);
        assertEquals(savedTaskModel.getId(), publishedEvent.id());
        assertEquals(savedTaskModel.getTitle(), publishedEvent.title());
        assertEquals(userModel, publishedEvent.user());
    }

    @Test
    void testUpdateTask_taskNotFound() throws TaskNotFoundException {

        //Given
        doThrow(new TaskNotFoundException()).when(taskRepositoryPort).getTaskById(updatedTaskModel.getId());

        ////When - Then
        assertThrows(TaskNotFoundException.class, () -> taskRepositoryPort.getTaskById(updatedTaskModel.getId()));

        verify(taskRepositoryPort).getTaskById(updatedTaskModel.getId());
        verify(taskRepositoryPort, never()).saveTask(any(TaskModel.class));
        verify(userClientRepositoryPort, never()).getUserById(savedTaskModel.getUserId());

    }
}
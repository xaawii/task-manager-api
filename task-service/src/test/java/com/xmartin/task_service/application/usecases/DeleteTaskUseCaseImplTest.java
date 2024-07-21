package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseImplTest {

    @InjectMocks
    private DeleteTaskUseCaseImpl deleteTaskUseCase;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @Mock
    private UserClientRepositoryPort userClientRepositoryPort;


    private TaskModel savedTaskModel;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        savedTaskModel = TaskModel.builder()
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
    void deleteTaskTest_canDelete() throws TaskNotFoundException {
        //Given
        Long id = 1L;

        when(taskRepositoryPort.getTaskById(id)).thenReturn(savedTaskModel);
        when(userClientRepositoryPort.getUserById(savedTaskModel.getUserId())).thenReturn(userModel);

        //When
        assertDoesNotThrow(() -> deleteTaskUseCase.deleteTask(id));

        //Then
        verify(taskRepositoryPort).getTaskById(id);
        verify(taskRepositoryPort).deleteTask(id);
        verify(userClientRepositoryPort).getUserById(savedTaskModel.getUserId());


        ArgumentCaptor<DeleteTaskEvent> eventCaptor = ArgumentCaptor.forClass(DeleteTaskEvent.class);
        verify(eventPublisherPort).publish(eventCaptor.capture());
        DeleteTaskEvent publishedEvent = eventCaptor.getValue();

        assertNotNull(publishedEvent);
        assertEquals(savedTaskModel.getId(), publishedEvent.id());
        assertEquals(savedTaskModel.getTitle(), publishedEvent.title());
        assertEquals(userModel, publishedEvent.user());

    }

    @Test
    void deleteTaskTest_taskNotFound() throws TaskNotFoundException {
        //Given
        Long id = 1L;

        //When
        doThrow(new TaskNotFoundException()).when(taskRepositoryPort).getTaskById(id);

        //Then
        assertThrows(TaskNotFoundException.class, () -> taskRepositoryPort.getTaskById(id));

        verify(taskRepositoryPort).getTaskById(id);
        verify(taskRepositoryPort, never()).deleteTask(id);


    }

}
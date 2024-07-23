package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.enums.Status;
import com.xmartin.task_service.domain.model.CreateTaskEvent;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskUseCaseImplTest {
    @InjectMocks
    private CreateTaskUseCaseImpl createTaskUseCase;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @Mock
    private UserClientRepositoryPort userClientRepositoryPort;

    private TaskModel taskModel;
    private TaskModel savedTaskModel;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        taskModel = TaskModel.builder()
                .title("Sample Task")
                .description("This is a sample task")
                .dueDate(now.plusDays(7))
                .status(Status.PENDING)
                .userId(1)
                .build();

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
    void testCreateTask() {

        //Given
        when(taskRepositoryPort.saveTask(any(TaskModel.class))).thenReturn(savedTaskModel);
        when(userClientRepositoryPort.getUserById(savedTaskModel.getUserId())).thenReturn(userModel);

        //when
        TaskModel result = createTaskUseCase.createTask(taskModel);

        //Then
        assertNotNull(result);
        assertEquals(savedTaskModel.getId(), result.getId());
        assertEquals(savedTaskModel.getTitle(), result.getTitle());

        verify(taskRepositoryPort).saveTask(any(TaskModel.class));
        verify(userClientRepositoryPort).getUserById(savedTaskModel.getUserId());

        ArgumentCaptor<CreateTaskEvent> eventCaptor = ArgumentCaptor.forClass(CreateTaskEvent.class);
        verify(eventPublisherPort).publish(eventCaptor.capture());
        CreateTaskEvent publishedEvent = eventCaptor.getValue();

        assertNotNull(publishedEvent);
        assertEquals(savedTaskModel.getId(), publishedEvent.id());
        assertEquals(savedTaskModel.getTitle(), publishedEvent.title());
        assertEquals(userModel, publishedEvent.user());
    }
}
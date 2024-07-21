package com.xmartin.notification_service.infraestructure.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.notification_service.application.services.NotificationService;
import com.xmartin.notification_service.domain.enums.Status;
import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.domain.model.UserModel;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskEventHandlerAdapterTest {

    @InjectMocks
    private TaskEventHandlerAdapter taskEventHandlerAdapter;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private NotificationService notificationService;

    private UserModel userModel;
    private String json;

    @BeforeEach
    void setUp() {

        userModel = new UserModel(1, "Xavi", "xavi@example.com", "ROLE_USER");


        json = """
                {
                    "id": 1,
                    "title": "Recoger ropa",
                    "description": "Tengo que recoger la ropa",
                    "createDate": "2024-07-21T00:00:00Z",
                    "updateDate": "2024-07-21T00:00:00Z",
                    "dueDate": "2024-07-22T00:00:00Z",
                    "status": "PENDING",
                    "user": {
                        "id": 1,
                        "name": "Xavi",
                        "email": "xavi@example.com",
                        "role": "ROLE_USER"
                    }
                }
                """;

    }

    @Test
    void testCreateTaskListener() throws JsonProcessingException, SendEmailException {
        // Given
        CreateTaskEvent createTaskEvent = new CreateTaskEvent(1L, "Recoger ropa",
                "Tengo que recoger la ropa", "2024-07-21T00:00:00Z",
                "2024-07-21T00:00:00Z", "2024-07-22T00:00:00Z",
                Status.PENDING, userModel);

        when(objectMapper.readValue(json, CreateTaskEvent.class)).thenReturn(createTaskEvent);

        // When
        taskEventHandlerAdapter.createTaskListener(json);

        // Then
        verify(objectMapper).readValue(json, CreateTaskEvent.class);
        verify(notificationService).sendTaskCreatedNotification(createTaskEvent);
    }

    @Test
    void testCreateTaskListener_JsonProcessingException() throws JsonProcessingException {
        // Given
        String json = "{\"title\": \"New Task\"}";
        when(objectMapper.readValue(json, CreateTaskEvent.class)).thenThrow(new JsonProcessingException("Error") {
        });

        // When and Then
        assertThrows(JsonProcessingException.class, () -> taskEventHandlerAdapter.createTaskListener(json));
    }

    @Test
    void testCreateTaskListener_SendEmailException() throws JsonProcessingException, SendEmailException {
        // Given
        CreateTaskEvent createTaskEvent = new CreateTaskEvent(1L, "Recoger ropa",
                "Tengo que recoger la ropa", "2024-07-21T00:00:00Z",
                "2024-07-21T00:00:00Z", "2024-07-22T00:00:00Z",
                Status.PENDING, userModel);

        when(objectMapper.readValue(json, CreateTaskEvent.class)).thenReturn(createTaskEvent);
        doThrow(new SendEmailException(new Exception())).when(notificationService).sendTaskCreatedNotification(createTaskEvent);

        // When and Then
        assertThrows(SendEmailException.class, () -> taskEventHandlerAdapter.createTaskListener(json));
    }

    @Test
    void testDeleteTaskListener() throws JsonProcessingException, SendEmailException {
        // Given
        DeleteTaskEvent deleteTaskEvent = new DeleteTaskEvent(1L, "Recoger ropa",
                "Tengo que recoger la ropa", "2024-07-21T00:00:00Z",
                "2024-07-21T00:00:00Z", "2024-07-22T00:00:00Z",
                Status.PENDING, userModel);

        when(objectMapper.readValue(json, DeleteTaskEvent.class)).thenReturn(deleteTaskEvent);

        // When
        taskEventHandlerAdapter.deleteTaskListener(json);

        // Then
        verify(objectMapper).readValue(json, DeleteTaskEvent.class);
        verify(notificationService).sendTaskDeletedNotification(deleteTaskEvent);
    }

    @Test
    void testUpdateTaskListener() throws JsonProcessingException, SendEmailException {
        // Given
        UpdateTaskEvent updateTaskEvent = new UpdateTaskEvent(1L, "Recoger ropa",
                "Tengo que recoger la ropa", "2024-07-21T00:00:00Z",
                "2024-07-21T00:00:00Z", "2024-07-22T00:00:00Z",
                Status.PENDING, userModel);

        when(objectMapper.readValue(json, UpdateTaskEvent.class)).thenReturn(updateTaskEvent);

        // When
        taskEventHandlerAdapter.updateTaskListener(json);

        // Then
        verify(objectMapper).readValue(json, UpdateTaskEvent.class);
        verify(notificationService).sendTaskUpdatedNotification(updateTaskEvent);
    }


}
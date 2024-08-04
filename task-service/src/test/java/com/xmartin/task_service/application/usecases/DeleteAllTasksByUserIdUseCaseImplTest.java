package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.UserNotFoundException;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteAllTasksByUserIdUseCaseImplTest {

    @InjectMocks
    private DeleteAllTasksByUserIdUseCaseImpl deleteAllTasksByUserIdUseCase;
    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private UserClientRepositoryPort userClientRepositoryPort;

    private UserModel userModel;

    @BeforeEach
    void setUp() {


        userModel = UserModel.builder()
                .id(1)
                .name("Xavi")
                .email("xavi@example.com")
                .role("ROLE_USER")
                .build();

    }

    @Test
    void deleteTasksByUserId_UserExists() {
        //Given
        Integer userId = 1;

        when(userClientRepositoryPort.getUserById(userId)).thenReturn(userModel);

        //When
        assertDoesNotThrow(() -> deleteAllTasksByUserIdUseCase.deleteTasksByUserId(userId));

        //Then
        verify(userClientRepositoryPort).getUserById(anyInt());
        verify(taskRepositoryPort).deleteAllTasksByUserId(anyInt());

    }

    @Test
    void deleteTasksByUserId_UserNotExists() {
        //Given
        Integer userId = 1;

        when(userClientRepositoryPort.getUserById(userId)).thenReturn(null);

        //When
        assertThrows(UserNotFoundException.class, () -> deleteAllTasksByUserIdUseCase.deleteTasksByUserId(userId));

        //Then
        verify(userClientRepositoryPort).getUserById(anyInt());
        verify(taskRepositoryPort, never()).deleteAllTasksByUserId(anyInt());

    }
}
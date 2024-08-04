package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import com.xmartin.userservice.infraestructure.client.TaskClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @Mock
    private UserServicePort userServicePort;

    @Mock
    private TaskClient taskClient;

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
    void deleteUser_success() throws UserNotFoundException {
        //Given
        String email = "xavi@example.com";
        when(userServicePort.getUserByEmail(email)).thenReturn(userModel);

        //When
        assertDoesNotThrow(() -> deleteUserUseCase.deleteUser(email));

        //Then
        verify(userServicePort).getUserByEmail(email);
        verify(taskClient).deleteAllTasksByUserId(userModel.getId());
        verify(userServicePort).deleteUser(email);
    }

    @Test
    void deleteUser_UserNotFound() throws UserNotFoundException {
        //Given
        String email = "xavi@example.com";
        doThrow(UserNotFoundException.class).when(userServicePort).getUserByEmail(email);

        //When
        assertThrows(UserNotFoundException.class, () -> deleteUserUseCase.deleteUser(email));

        //Then
        verify(userServicePort).getUserByEmail(email);
        verify(taskClient, never()).deleteAllTasksByUserId(userModel.getId());
        verify(userServicePort, never()).deleteUser(email);
    }
}
package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    @Mock
    private UserServicePort userServicePort;

    private UserModel userModel;
    private UserModel savedUserModel;


    @BeforeEach
    void setUp() {

        userModel = UserModel.builder()
                .name("Carlos")
                .email("xavi@example.com")
                .password("1234")
                .role("ROLE_USER")
                .build();

        savedUserModel = UserModel.builder()
                .id(1)
                .name("Xavi")
                .email("xavi@example.com")
                .password("abcde")
                .role("ROLE_USER")
                .build();

    }

    @Test
    void updateUserTest_success() throws UserNotFoundException {

        //Given
        Integer userId = 1;
        when(userServicePort.getUserById(userId)).thenReturn(savedUserModel);
        when(userServicePort.save(savedUserModel)).thenReturn(savedUserModel);

        //When
        UserModel result = assertDoesNotThrow(() -> updateUserUseCase.updateUser(userModel, userId));

        //Then
        assertNotNull(result);
        assertEquals(userModel.getName(), result.getName());
        assertEquals(userModel.getPassword(), result.getPassword());
        assertEquals(userId, result.getId());

        verify(userServicePort).getUserById(userId);
        verify(userServicePort).save(savedUserModel);
    }

    @Test
    void updateUserTest_UserNotFoundException() throws UserNotFoundException {

        //Given
        Integer userId = 1;
        doThrow(UserNotFoundException.class).when(userServicePort).getUserById(userId);


        //When
        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.updateUser(userModel, userId));

        //Then

        verify(userServicePort).getUserById(userId);
        verify(userServicePort, never()).save(savedUserModel);
    }
}
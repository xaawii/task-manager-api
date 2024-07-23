package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
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
class SaveUserUseCaseImplTest {
    @InjectMocks
    private SaveUserUseCaseImpl saveUserUseCase;

    @Mock
    private UserServicePort userServicePort;

    private UserModel userModel;
    private UserModel savedUserModel;


    @BeforeEach
    void setUp() {

        userModel = UserModel.builder()
                .name("Xavi")
                .email("xavi@example.com")
                .role("ROLE_USER")
                .build();

        savedUserModel = UserModel.builder()
                .id(1)
                .name("Xavi")
                .email("xavi@example.com")
                .role("ROLE_USER")
                .build();

    }

    @Test
    void saveUser_success() {

        //Given
        when(userServicePort.userExists(userModel.getEmail())).thenReturn(false);
        when(userServicePort.save(userModel)).thenReturn(savedUserModel);

        //When
        UserModel result = assertDoesNotThrow(() -> saveUserUseCase.saveUser(userModel));

        //Then
        assertNotNull(result);
        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getRole(), result.getRole());
        assertEquals(1, result.getId());

        verify(userServicePort).userExists(userModel.getEmail());
        verify(userServicePort).save(userModel);


    }

    @Test
    void saveUser_EmailAlreadyInUseException() {

        //Given
        when(userServicePort.userExists(userModel.getEmail())).thenReturn(true);

        //When
        assertThrows(EmailAlreadyInUseException.class, () -> saveUserUseCase.saveUser(userModel));

        //Then
        verify(userServicePort).userExists(userModel.getEmail());
        verify(userServicePort, never()).save(userModel);

    }

}
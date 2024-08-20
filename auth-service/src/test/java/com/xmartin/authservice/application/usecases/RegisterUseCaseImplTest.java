package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RegisterUserEvent;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseImplTest {

    @InjectMocks
    private RegisterUseCaseImpl registerUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserClientPort userClientPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    private UserModel userModel;

    @BeforeEach
    void setUp() {

        userModel = UserModel.builder()
                .email("xavi@test.com")
                .name("Xavi")
                .build();
    }

    @Test
    void register() throws EmailAlreadyInUseException {
        //Given
        RegisterModel registerModel = RegisterModel.builder()
                .email("xavi@test.com")
                .name("Xavi")
                .password("password")
                .build();

        when(passwordEncoder.encode(registerModel.getPassword())).thenReturn("encodedPassword");
        when(userClientPort.save(any(UserModel.class))).thenReturn(userModel);

        //When
        UserModel savedUser = registerUseCase.register(registerModel);

        //Then
        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userClientPort).save(userCaptor.capture());
        UserModel userModelCaptured = userCaptor.getValue();

        ArgumentCaptor<RegisterUserEvent> eventCaptor = ArgumentCaptor.forClass(RegisterUserEvent.class);
        verify(eventPublisherPort).publish(eventCaptor.capture());
        RegisterUserEvent registerUserEvent = eventCaptor.getValue();

        assertEquals(userModelCaptured.getEmail(), registerUserEvent.user().getEmail());
        assertEquals("encodedPassword", userModelCaptured.getPassword());
        assertEquals(registerModel.getName(), userModelCaptured.getName());

        assertNotNull(savedUser);
        assertEquals(registerModel.getEmail(), savedUser.getEmail());


    }
}
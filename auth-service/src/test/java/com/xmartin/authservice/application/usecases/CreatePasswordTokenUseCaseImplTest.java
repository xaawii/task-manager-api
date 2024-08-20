package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.PasswordTokenProvider;
import com.xmartin.authservice.domain.model.PasswordTokenEvent;
import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.domain.ports.out.PasswordTokenRepositoryPort;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePasswordTokenUseCaseImplTest {

    @InjectMocks
    private CreatePasswordTokenUseCaseImpl createPasswordTokenUseCase;

    @Mock
    private PasswordTokenRepositoryPort passwordTokenRepositoryPort;

    @Mock
    private PasswordTokenProvider passwordTokenProvider;

    @Mock
    private UserClientPort userClientPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    private PasswordTokenModel passwordTokenModel;
    private PasswordTokenModel savedPasswordTokenModel;

    @BeforeEach
    void setUp() {

        passwordTokenModel = PasswordTokenModel.builder()
                .email("xavi@example.com")
                .expiryDate(LocalDateTime.now())
                .token("token")
                .build();

        savedPasswordTokenModel = PasswordTokenModel.builder()
                .id(1L)
                .email("xavi@example.com")
                .expiryDate(LocalDateTime.now())
                .token("token")
                .build();

    }

    @Test
    void createPasswordTokenTest() {

        //Given
        String email = "xavi@example.com";
        when(userClientPort.getUserExistsByEmail(email)).thenReturn(true);
        when(passwordTokenProvider.generatePasswordToken(email)).thenReturn(passwordTokenModel);
        when(passwordTokenRepositoryPort.save(passwordTokenModel)).thenReturn(savedPasswordTokenModel);

        //When
        assertDoesNotThrow(() -> createPasswordTokenUseCase.createPasswordToken(email));

        //Then

        ArgumentCaptor<PasswordTokenEvent> eventCaptor = ArgumentCaptor.forClass(PasswordTokenEvent.class);
        verify(eventPublisherPort).publish(eventCaptor.capture());
        PasswordTokenEvent publishedEvent = eventCaptor.getValue();

        assertNotNull(publishedEvent);
        assertEquals(savedPasswordTokenModel.getId(), publishedEvent.id());
        assertEquals(savedPasswordTokenModel.getEmail(), publishedEvent.email());
        assertEquals("token", publishedEvent.token());

        verify(userClientPort).getUserExistsByEmail(email);
        verify(passwordTokenProvider).generatePasswordToken(email);


    }

}
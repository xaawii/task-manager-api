package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.PasswordTokenProvider;
import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.domain.model.ResetPasswordEvent;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.domain.ports.out.PasswordTokenRepositoryPort;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordUseCaseImplTest {

    @InjectMocks
    private ResetPasswordUseCaseImpl resetPasswordUseCase;

    @Mock
    private PasswordTokenRepositoryPort passwordTokenRepositoryPort;

    @Mock
    private PasswordTokenProvider passwordTokenProvider;

    @Mock
    private UserClientPort userClientPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    private PasswordTokenModel savedPasswordTokenModel;
    private UserModel userModel;

    @BeforeEach
    void setUp() {

        savedPasswordTokenModel = PasswordTokenModel.builder()
                .id(1L)
                .email("xavi@example.com")
                .expiryDate(LocalDateTime.now())
                .token("token")
                .build();

        userModel = UserModel.builder()
                .id(1)
                .name("Xavi")
                .email("xavi@example.com")
                .password("123456")
                .build();

    }

    @Test
    void resetPasswordTest_success() throws TokenNotFoundException {
        //Given
        String token = "token";
        String password = "abcde";

        when(passwordTokenRepositoryPort.findByToken(token)).thenReturn(savedPasswordTokenModel);
        when(passwordTokenProvider.isTokenExpired(savedPasswordTokenModel.getExpiryDate())).thenReturn(false);
        when(userClientPort.getUserByEmail(savedPasswordTokenModel.getEmail())).thenReturn(Optional.ofNullable(userModel));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        //When
        assertDoesNotThrow(() -> resetPasswordUseCase.resetPassword(token, password));

        //Then

        assertEquals("encodedPassword", userModel.getPassword());

        verify(userClientPort).updateUser(userModel, userModel.getId());
        verify(passwordTokenRepositoryPort).remove(savedPasswordTokenModel.getToken());
        verify(eventPublisherPort).publish(any(ResetPasswordEvent.class));
    }

}
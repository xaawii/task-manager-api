package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.JwtProvider;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserClientPort userClientPort;

    @Mock
    private JwtProvider jwtProvider;

    private UserModel userModel;

    @BeforeEach
    void setUp() {

        userModel = UserModel.builder()
                .email("xavi@test.com")
                .name("Xavi")
                .build();
    }

    @Test
    void login_success() {

        //Given
        LoginModel loginModel = LoginModel.builder()
                .email("xavi@test.com")
                .password("password")
                .build();

        when(userClientPort.getUserByEmail(loginModel.getEmail())).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches(loginModel.getPassword(), userModel.getPassword())).thenReturn(true);
        when(jwtProvider.createToken(userModel)).thenReturn("newToken");

        //When
        String token = assertDoesNotThrow(() -> loginUseCase.login(loginModel));

        //Then
        assertEquals("newToken", token);
        verify(userClientPort, times(1)).getUserByEmail(loginModel.getEmail());
    }

    @Test
    void login_UserNotFound() {
        //Given
        LoginModel loginModel = LoginModel.builder()
                .email("xavi@test.com")
                .password("password")
                .build();

        when(userClientPort.getUserByEmail(loginModel.getEmail())).thenReturn(Optional.empty());

        //When - Then
        assertThrows(UserNotFoundException.class, () -> loginUseCase.login(loginModel));
    }

    @Test
    void login_WrongPassword() {

        //Given
        LoginModel loginModel = LoginModel.builder()
                .email("xavi@test.com")
                .password("password")
                .build();


        when(userClientPort.getUserByEmail(loginModel.getEmail())).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches(loginModel.getPassword(), userModel.getPassword())).thenReturn(false);

        //When- Then
        assertThrows(WrongPasswordException.class, () -> loginUseCase.login(loginModel));
    }
}
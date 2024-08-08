package com.xmartin.authservice.infraestructure.adapters;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.infraestructure.client.UserClient;
import com.xmartin.authservice.infraestructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceAdapterTest {

    @InjectMocks
    private AuthUserServiceAdapter authUserServiceAdapter;

    @Mock
    private UserClient userClient;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    void save() {
        //Given
        RegisterModel registerModel = RegisterModel.builder()
                .email("xavi@test.com")
                .name("Xavi")
                .password("password")
                .build();

        when(passwordEncoder.encode(registerModel.getPassword())).thenReturn("encodedPassword");
        when(userClient.saveUser(any(UserModel.class))).thenReturn(userModel);

        //When
        UserModel savedUser = authUserServiceAdapter.save(registerModel);

        //Then
        assertNotNull(savedUser);
        assertEquals(registerModel.getEmail(), savedUser.getEmail());
        verify(userClient, times(1)).saveUser(any(UserModel.class));
    }

    @Test
    void login_success() {

        //Given
        LoginModel loginModel = LoginModel.builder()
                .email("xavi@test.com")
                .password("password")
                .build();

        when(userClient.getUserByEmail(loginModel.getEmail())).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches(loginModel.getPassword(), userModel.getPassword())).thenReturn(true);
        when(jwtProvider.createToken(userModel)).thenReturn("newToken");

        //When
        String token = assertDoesNotThrow(() -> authUserServiceAdapter.login(loginModel));

        //Then
        assertEquals("newToken", token);
        verify(userClient, times(1)).getUserByEmail(loginModel.getEmail());
    }

    @Test
    void login_UserNotFound() {
        //Given
        LoginModel loginModel = LoginModel.builder()
                .email("xavi@test.com")
                .password("password")
                .build();

        when(userClient.getUserByEmail(loginModel.getEmail())).thenReturn(Optional.empty());

        //When - Then
        assertThrows(UserNotFoundException.class, () -> authUserServiceAdapter.login(loginModel));
    }

    @Test
    void login_WrongPassword() {

        //Given
        LoginModel loginModel = LoginModel.builder()
                .email("xavi@test.com")
                .password("password")
                .build();


        when(userClient.getUserByEmail(loginModel.getEmail())).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches(loginModel.getPassword(), userModel.getPassword())).thenReturn(false);

        //When- Then
        assertThrows(WrongPasswordException.class, () -> authUserServiceAdapter.login(loginModel));
    }

    @Test
    void validate_success() throws InvalidTokenException {
        //Given
        RequestModel requestModel = new RequestModel();
        String token = "token";
        String email = "xavi@test.com";

        when(jwtProvider.validate(token, requestModel)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(token)).thenReturn(email);
        when(userClient.getUserExistsByEmail(email)).thenReturn(true);

        //When
        String validatedToken = authUserServiceAdapter.validateRequestAndToken(token, requestModel);

        //Then
        assertEquals(token, validatedToken);
        verify(jwtProvider, times(1)).validate(token, requestModel);
        verify(userClient, times(1)).getUserExistsByEmail(email);
    }

    @Test
    void validate_InvalidToken() {
        //Given
        RequestModel requestModel = new RequestModel();
        String token = "token";

        when(jwtProvider.validate(token, requestModel)).thenReturn(false);

        //When - Then
        assertThrows(InvalidTokenException.class, () -> authUserServiceAdapter.validateRequestAndToken(token, requestModel));
    }

    @Test
    void validateToken_success() throws InvalidTokenException {
        //Given
        String token = "token";
        String email = "xavi@test.com";

        when(jwtProvider.validateOnlyToken(token)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(token)).thenReturn(email);
        when(userClient.getUserExistsByEmail(email)).thenReturn(true);

        //When
        String validatedToken = authUserServiceAdapter.validateToken(token);

        //Then
        assertEquals(token, validatedToken);
        verify(jwtProvider, times(1)).validateOnlyToken(token);
        verify(userClient, times(1)).getUserExistsByEmail(email);
    }

    @Test
    void validateToken_InvalidToken() {
        //Given
        String token = "token";

        when(jwtProvider.validateOnlyToken(token)).thenReturn(false);

        //When - Then
        assertThrows(InvalidTokenException.class, () -> authUserServiceAdapter.validateToken(token));
        verify(userClient, never()).getUserExistsByEmail(anyString());
    }
}
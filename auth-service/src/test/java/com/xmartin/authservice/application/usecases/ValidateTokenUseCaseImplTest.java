package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.JwtProvider;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateTokenUseCaseImplTest {

    @InjectMocks
    private ValidateTokenUseCaseImpl validateTokenUseCase;
    @Mock
    private UserClientPort userClientPort;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    void validateToken_success() throws InvalidTokenException {
        //Given
        String token = "token";
        String email = "xavi@test.com";

        when(jwtProvider.validateOnlyToken(token)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(token)).thenReturn(email);
        when(userClientPort.getUserExistsByEmail(email)).thenReturn(true);

        //When
        String validatedToken = validateTokenUseCase.validateToken(token);

        //Then
        assertEquals(token, validatedToken);
        verify(jwtProvider, times(1)).validateOnlyToken(token);
        verify(userClientPort, times(1)).getUserExistsByEmail(email);
    }

    @Test
    void validateToken_InvalidToken() {
        //Given
        String token = "token";

        when(jwtProvider.validateOnlyToken(token)).thenReturn(false);

        //When - Then
        assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validateToken(token));
        verify(userClientPort, never()).getUserExistsByEmail(anyString());
    }
}
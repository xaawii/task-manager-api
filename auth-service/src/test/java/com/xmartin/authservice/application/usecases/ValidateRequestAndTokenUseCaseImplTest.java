package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.JwtProvider;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.model.RequestModel;
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
class ValidateRequestAndTokenUseCaseImplTest {
    @InjectMocks
    private ValidateRequestAndTokenUseCaseImpl validateRequestAndTokenUseCase;

    @Mock
    private UserClientPort userClientPort;

    @Mock
    private JwtProvider jwtProvider;


    @Test
    void validate_success() throws InvalidTokenException {
        //Given
        RequestModel requestModel = new RequestModel();
        String token = "token";
        String email = "xavi@test.com";

        when(jwtProvider.validate(token, requestModel)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(token)).thenReturn(email);
        when(userClientPort.getUserExistsByEmail(email)).thenReturn(true);

        //When
        String validatedToken = validateRequestAndTokenUseCase.validate(token, requestModel);

        //Then
        assertEquals(token, validatedToken);
        verify(jwtProvider, times(1)).validate(token, requestModel);
        verify(userClientPort, times(1)).getUserExistsByEmail(email);
    }

    @Test
    void validate_InvalidToken() {
        //Given
        RequestModel requestModel = new RequestModel();
        String token = "token";

        when(jwtProvider.validate(token, requestModel)).thenReturn(false);

        //When - Then
        assertThrows(InvalidTokenException.class, () -> validateRequestAndTokenUseCase.validate(token, requestModel));
    }
}
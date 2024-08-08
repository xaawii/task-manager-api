package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.ports.in.ValidateTokenUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTokenUseCaseImpl implements ValidateTokenUseCase {
    private final AuthUserServicePort authUserServicePort;

    @Override
    public String validateToken(String token) throws InvalidTokenException {
        return authUserServicePort.validateToken(token);
    }
}

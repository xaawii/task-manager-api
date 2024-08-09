package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.JwtProvider;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.ports.in.ValidateTokenUseCase;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTokenUseCaseImpl implements ValidateTokenUseCase {
    private final UserClientPort userClientPort;
    private final JwtProvider jwtProvider;

    @Override
    public String validateToken(String token) throws InvalidTokenException {

        if (!jwtProvider.validateOnlyToken(token)) throw new InvalidTokenException();

        String email = jwtProvider.getEmailFromToken(token);
        if (!userClientPort.getUserExistsByEmail(email)) throw new InvalidTokenException();

        return token;
    }
}

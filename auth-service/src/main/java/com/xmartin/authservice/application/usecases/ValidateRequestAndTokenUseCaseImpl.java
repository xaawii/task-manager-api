package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.JwtProvider;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.ports.in.ValidateRequestAndTokenUseCase;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateRequestAndTokenUseCaseImpl implements ValidateRequestAndTokenUseCase {

    private final UserClientPort userClientPort;
    private final JwtProvider jwtProvider;

    @Override
    public String validate(String token, RequestModel requestModel) throws InvalidTokenException {

        if (!jwtProvider.validate(token, requestModel)) throw new InvalidTokenException();

        String email = jwtProvider.getEmailFromToken(token);

        if (!userClientPort.getUserExistsByEmail(email)) throw new InvalidTokenException();

        return token;
    }
}

package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.ports.in.ValidateRequestAndTokenUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateRequestAndTokenUseCaseImpl implements ValidateRequestAndTokenUseCase {

    private final AuthUserServicePort authUserServicePort;

    @Override
    public String validate(String token, RequestModel requestModel) throws InvalidTokenException {
        return authUserServicePort.validateRequestAndToken(token, requestModel);
    }
}

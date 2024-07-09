package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.ports.in.ValidateUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateUseCaseImpl implements ValidateUseCase {

    private final AuthUserServicePort authUserServicePort;

    @Override
    public TokenDto validate(String token, RequestDto requestDto) {
        return authUserServicePort.validate(token, requestDto);
    }
}

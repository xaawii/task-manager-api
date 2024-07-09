package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.ports.in.LoginUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthUserServicePort authUserServicePort;

    @Override
    public TokenDto login(LoginDto loginDto) {
        return authUserServicePort.login(loginDto);
    }
}

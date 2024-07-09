package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.RegisterUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCaseImpl implements RegisterUseCase {

    private final AuthUserServicePort authUserServicePort;

    @Override
    public UserModel register(RegisterDto registerDto) {
        return authUserServicePort.save(registerDto);
    }
}

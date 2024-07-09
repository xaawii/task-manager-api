package com.xmartin.authservice.application.service;

import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.LoginUseCase;
import com.xmartin.authservice.domain.ports.in.RegisterUseCase;
import com.xmartin.authservice.domain.ports.in.ValidateUseCase;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase, RegisterUseCase, ValidateUseCase {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final ValidateUseCase validateUseCase;

    @Override
    public TokenDto login(LoginDto loginDto) {
        return loginUseCase.login(loginDto);
    }

    @Override
    public UserModel register(RegisterDto registerDto) {
        return registerUseCase.register(registerDto);
    }

    @Override
    public TokenDto validate(String token, RequestDto requestDto) {
        return validateUseCase.validate(token, requestDto);
    }
}

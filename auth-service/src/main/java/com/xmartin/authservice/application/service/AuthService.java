package com.xmartin.authservice.application.service;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.LoginUseCase;
import com.xmartin.authservice.domain.ports.in.RegisterUseCase;
import com.xmartin.authservice.domain.ports.in.ValidateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase, RegisterUseCase, ValidateUseCase {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final ValidateUseCase validateUseCase;

    @Override
    public String login(LoginModel loginModel) throws UserNotFoundException, WrongPasswordException {
        return loginUseCase.login(loginModel);
    }

    @Override
    public UserModel register(RegisterModel registerModel) throws EmailAlreadyInUseException {
        return registerUseCase.register(registerModel);
    }

    @Override
    public String validate(String token, RequestModel requestModel) throws InvalidTokenException {
        return validateUseCase.validate(token, requestModel);
    }
}

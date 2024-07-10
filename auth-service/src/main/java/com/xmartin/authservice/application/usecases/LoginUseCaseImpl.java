package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.ports.in.LoginUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthUserServicePort authUserServicePort;

    @Override
    public String login(LoginModel loginModel) throws UserNotFoundException, WrongPasswordException {
        return authUserServicePort.login(loginModel);
    }
}

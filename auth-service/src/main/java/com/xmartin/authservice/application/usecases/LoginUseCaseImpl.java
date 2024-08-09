package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.JwtProvider;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.LoginUseCase;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class LoginUseCaseImpl implements LoginUseCase {

    private final UserClientPort userClientPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public String login(LoginModel loginModel) throws UserNotFoundException, WrongPasswordException {
        Optional<UserModel> user = userClientPort.getUserByEmail(loginModel.getEmail());

        if (user.isEmpty()) throw new UserNotFoundException();
        if (passwordEncoder.matches(loginModel.getPassword(), user.get().getPassword())) {
            return jwtProvider.createToken(user.get());
        } else {
            throw new WrongPasswordException();
        }
    }
}

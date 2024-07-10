package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.RegisterUseCase;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCaseImpl implements RegisterUseCase {

    private final AuthUserServicePort authUserServicePort;

    @Override
    public UserModel register(RegisterModel registerModel) throws EmailAlreadyInUseException {
        return authUserServicePort.save(registerModel);
    }
}

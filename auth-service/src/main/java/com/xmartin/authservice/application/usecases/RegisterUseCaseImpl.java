package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RegisterUserEvent;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.RegisterUseCase;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCaseImpl implements RegisterUseCase {

    private final UserClientPort userClientPort;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public UserModel register(RegisterModel registerModel) throws EmailAlreadyInUseException {
        String password = passwordEncoder.encode(registerModel.getPassword());

        UserModel userModel = UserModel.builder()
                .name(registerModel.getName())
                .email(registerModel.getEmail())
                .password(password)
                .role("ROLE_USER")
                .build();

        RegisterUserEvent registerUserEvent = new RegisterUserEvent(userModel);
        eventPublisherPort.publish(registerUserEvent);

        return userClientPort.save(userModel);
    }
}

package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.SaveUserUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveUserUseCaseImpl implements SaveUserUseCase {

    private final UserServicePort userServicePort;

    @Override
    public UserModel saveUser(UserModel user) throws EmailAlreadyInUseException {
        if (userServicePort.userExists(user.getEmail())) throw new EmailAlreadyInUseException(user.getEmail());
        return userServicePort.save(user);
    }
}

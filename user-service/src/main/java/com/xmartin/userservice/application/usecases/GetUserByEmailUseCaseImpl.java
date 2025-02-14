package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.GetUserByEmailUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserByEmailUseCaseImpl implements GetUserByEmailUseCase {

    private final UserServicePort userServicePort;

    @Override
    public UserModel getUserByEmail(String email) throws UserNotFoundException {
        return userServicePort.getUserByEmail(email);
    }
}

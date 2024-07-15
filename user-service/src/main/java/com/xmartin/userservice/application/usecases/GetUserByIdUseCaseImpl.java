package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.GetUserByIdUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {

    private final UserServicePort userServicePort;

    @Override
    public UserModel getUserById(Integer userId) throws UserNotFoundException {
        return userServicePort.getUserById(userId);
    }
}

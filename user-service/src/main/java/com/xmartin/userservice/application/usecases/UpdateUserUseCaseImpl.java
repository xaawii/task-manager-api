package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.UpdateUserUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserServicePort userServicePort;

    @Override
    public UserModel updateUser(UserModel userModel, Integer userId) throws UserNotFoundException {
        UserModel userFromDb = userServicePort.getUserById(userId);
        userFromDb.setName(userModel.getName());
        userFromDb.setPassword(userModel.getPassword());
        return userServicePort.save(userFromDb);
    }
}

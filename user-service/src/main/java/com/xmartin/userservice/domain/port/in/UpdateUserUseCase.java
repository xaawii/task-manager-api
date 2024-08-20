package com.xmartin.userservice.domain.port.in;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;

public interface UpdateUserUseCase {
    UserModel updateUser(UserModel userModel, Integer userId) throws UserNotFoundException;
}

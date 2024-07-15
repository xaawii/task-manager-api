package com.xmartin.userservice.domain.port.in;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;

public interface GetUserByIdUseCase {
    UserModel getUserById(Integer userId) throws UserNotFoundException;
}

package com.xmartin.userservice.domain.port.in;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;

public interface GetUserUseCase {
    UserModel getUserByEmail(String email) throws UserNotFoundException;
}

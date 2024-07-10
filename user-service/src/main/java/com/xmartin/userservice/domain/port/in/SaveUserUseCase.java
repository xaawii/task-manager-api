package com.xmartin.userservice.domain.port.in;

import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.model.UserModel;

public interface SaveUserUseCase {
    UserModel saveUser(UserModel user) throws EmailAlreadyInUseException;
}

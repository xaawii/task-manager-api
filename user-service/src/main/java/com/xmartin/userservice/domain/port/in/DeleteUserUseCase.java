package com.xmartin.userservice.domain.port.in;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;

public interface DeleteUserUseCase {
    void deleteUser(String email) throws UserNotFoundException;
}

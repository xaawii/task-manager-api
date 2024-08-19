package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.UserNotFoundException;

public interface CreatePasswordTokenUseCase {
    void createPasswordToken(String email) throws UserNotFoundException;
}

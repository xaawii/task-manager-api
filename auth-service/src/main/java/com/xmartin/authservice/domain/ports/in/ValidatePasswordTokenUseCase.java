package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;

public interface ValidatePasswordTokenUseCase {
    Boolean validatePasswordToken(String token) throws TokenNotFoundException;
}

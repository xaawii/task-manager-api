package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;

public interface ValidateTokenUseCase {
    String validateToken(String token) throws InvalidTokenException;
}

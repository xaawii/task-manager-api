package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;

public interface ResetPasswordUseCase {
    void resetPassword(String token, String newPassword) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException;
}

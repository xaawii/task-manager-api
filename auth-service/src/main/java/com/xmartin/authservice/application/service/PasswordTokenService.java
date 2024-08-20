package com.xmartin.authservice.application.service;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.ports.in.CreatePasswordTokenUseCase;
import com.xmartin.authservice.domain.ports.in.ResetPasswordUseCase;
import com.xmartin.authservice.domain.ports.in.ValidatePasswordTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordTokenService implements CreatePasswordTokenUseCase, ResetPasswordUseCase, ValidatePasswordTokenUseCase {
    private final CreatePasswordTokenUseCase createPasswordTokenUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ValidatePasswordTokenUseCase validatePasswordTokenUseCase;

    @Override
    public void createPasswordToken(String email) throws UserNotFoundException {
        createPasswordTokenUseCase.createPasswordToken(email);
    }

    @Override
    public void resetPassword(String token, String newPassword) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException {
        resetPasswordUseCase.resetPassword(token, newPassword);
    }


    @Override
    public Boolean validatePasswordToken(String token) throws TokenNotFoundException {
        return validatePasswordTokenUseCase.validatePasswordToken(token);
    }
}

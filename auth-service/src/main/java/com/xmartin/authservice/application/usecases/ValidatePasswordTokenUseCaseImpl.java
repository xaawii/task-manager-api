package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.PasswordTokenProvider;
import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.domain.ports.in.ValidatePasswordTokenUseCase;
import com.xmartin.authservice.domain.ports.out.PasswordTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ValidatePasswordTokenUseCaseImpl implements ValidatePasswordTokenUseCase {

    private final PasswordTokenRepositoryPort passwordTokenRepositoryPort;
    private final PasswordTokenProvider passwordTokenProvider;

    @Transactional
    @Override
    public Boolean validatePasswordToken(String token) throws TokenNotFoundException {
        PasswordTokenModel passwordTokenModel = passwordTokenRepositoryPort.findByToken(token);
        boolean isExpired = passwordTokenProvider.isTokenExpired(passwordTokenModel.getExpiryDate());

        if (isExpired) {
            passwordTokenRepositoryPort.remove(passwordTokenModel.getToken());
        }
        return !isExpired;
    }
}

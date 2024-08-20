package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.PasswordTokenProvider;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.domain.model.ResetPasswordEvent;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.in.ResetPasswordUseCase;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.domain.ports.out.PasswordTokenRepositoryPort;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResetPasswordUseCaseImpl implements ResetPasswordUseCase {
    private final PasswordTokenRepositoryPort passwordTokenRepositoryPort;
    private final UserClientPort userClientPort;
    private final PasswordTokenProvider passwordTokenProvider;
    private final EventPublisherPort eventPublisherPort;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException {
        PasswordTokenModel passwordTokenModel = passwordTokenRepositoryPort.findByToken(token);

        if (passwordTokenProvider.isTokenExpired(passwordTokenModel.getExpiryDate())) {
            passwordTokenRepositoryPort.remove(passwordTokenModel.getToken());
            throw new InvalidTokenException();
        }

        UserModel userModel = userClientPort.getUserByEmail(passwordTokenModel.getEmail()).orElseThrow(UserNotFoundException::new);

        userModel.setPassword(passwordEncoder.encode(newPassword));

        userClientPort.updateUser(userModel, userModel.getId());

        ResetPasswordEvent resetPasswordEvent = new ResetPasswordEvent(userModel);

        passwordTokenRepositoryPort.remove(passwordTokenModel.getToken());
        eventPublisherPort.publish(resetPasswordEvent);


    }
}

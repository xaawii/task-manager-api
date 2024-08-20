package com.xmartin.authservice.application.usecases;

import com.xmartin.authservice.application.service.PasswordTokenProvider;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.model.PasswordTokenEvent;
import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.domain.ports.in.CreatePasswordTokenUseCase;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.domain.ports.out.PasswordTokenRepositoryPort;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePasswordTokenUseCaseImpl implements CreatePasswordTokenUseCase {

    private final PasswordTokenRepositoryPort passwordTokenRepositoryPort;
    private final UserClientPort userClientPort;
    private final PasswordTokenProvider passwordTokenProvider;
    private final EventPublisherPort eventPublisherPort;

    @Transactional
    @Override
    public void createPasswordToken(String email) throws UserNotFoundException {

        if (!userClientPort.getUserExistsByEmail(email)) throw new UserNotFoundException();

        PasswordTokenModel passwordTokenModel = passwordTokenRepositoryPort
                .save(passwordTokenProvider.generatePasswordToken(email));

        PasswordTokenEvent passwordTokenEvent =
                new PasswordTokenEvent(passwordTokenModel.getId(), passwordTokenModel.getToken(), passwordTokenModel.getEmail());

        eventPublisherPort.publish(passwordTokenEvent);


    }
}

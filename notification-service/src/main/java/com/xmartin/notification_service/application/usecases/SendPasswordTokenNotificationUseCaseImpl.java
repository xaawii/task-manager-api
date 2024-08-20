package com.xmartin.notification_service.application.usecases;

import com.xmartin.notification_service.domain.model.PasswordTokenEvent;
import com.xmartin.notification_service.domain.port.in.SendPasswordTokenNotificationUseCase;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendPasswordTokenNotificationUseCaseImpl implements SendPasswordTokenNotificationUseCase {
    private final MailSenderPort mailSenderPort;

    @Override
    public void sendPasswordTokenNotification(PasswordTokenEvent event) throws SendEmailException {
        mailSenderPort.sendPasswordTokenNotification(event);
    }
}

package com.xmartin.notification_service.application.usecases;

import com.xmartin.notification_service.domain.model.ResetPasswordEvent;
import com.xmartin.notification_service.domain.port.in.SendResetPasswordNotificationUseCase;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendResetPasswordNotificationUseCaseImpl implements SendResetPasswordNotificationUseCase {
    private final MailSenderPort mailSenderPort;

    @Override
    public void sendResetPasswordNotificationUseCase(ResetPasswordEvent event) throws SendEmailException {
        mailSenderPort.sendResetPasswordNotification(event);
    }
}

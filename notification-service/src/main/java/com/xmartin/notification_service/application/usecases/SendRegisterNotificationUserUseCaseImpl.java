package com.xmartin.notification_service.application.usecases;

import com.xmartin.notification_service.domain.model.RegisterUserEvent;
import com.xmartin.notification_service.domain.port.in.SendRegisterNotificationUserUseCase;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendRegisterNotificationUserUseCaseImpl implements SendRegisterNotificationUserUseCase {
    private final MailSenderPort mailSenderPort;
    @Override
    public void sendRegisterUser(RegisterUserEvent event) throws SendEmailException {
        mailSenderPort.sendRegisterUserNotification(event);
    }
}

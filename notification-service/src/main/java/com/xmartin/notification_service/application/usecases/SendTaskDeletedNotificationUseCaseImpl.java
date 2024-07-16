package com.xmartin.notification_service.application.usecases;

import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.port.in.SendTaskDeletedNotificationUseCase;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendTaskDeletedNotificationUseCaseImpl implements SendTaskDeletedNotificationUseCase {

    private final MailSenderPort mailSenderPort;
    @Override
    public void sendTaskDeletedNotification(DeleteTaskEvent deleteTaskEvent) throws SendEmailException {
        mailSenderPort.sendDeleteNotification(deleteTaskEvent);
    }
}

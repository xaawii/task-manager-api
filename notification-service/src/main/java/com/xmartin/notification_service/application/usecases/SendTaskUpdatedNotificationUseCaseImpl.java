package com.xmartin.notification_service.application.usecases;

import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.domain.port.in.SendTaskUpdatedNotificationUseCase;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendTaskUpdatedNotificationUseCaseImpl implements SendTaskUpdatedNotificationUseCase {

    private final MailSenderPort mailSenderPort;

    @Override
    public void sendTaskUpdatedNotification(UpdateTaskEvent updateTaskEvent) throws SendEmailException {
        mailSenderPort.sendUpdateNotification(updateTaskEvent);
    }
}

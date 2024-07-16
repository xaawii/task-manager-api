package com.xmartin.notification_service.application.usecases;

import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.port.in.SendTaskCreatedNotificationUseCase;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendTaskCreatedNotificationUseCaseImpl implements SendTaskCreatedNotificationUseCase {

    private final MailSenderPort mailSenderPort;


    @Override
    public void sendTaskCreatedNotification(CreateTaskEvent createTaskEvent) throws SendEmailException {
        mailSenderPort.sendCreateNotification(createTaskEvent);
    }
}

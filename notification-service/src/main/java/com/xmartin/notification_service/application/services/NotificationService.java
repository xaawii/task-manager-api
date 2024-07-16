package com.xmartin.notification_service.application.services;

import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.domain.port.in.SendTaskCreatedNotificationUseCase;
import com.xmartin.notification_service.domain.port.in.SendTaskDeletedNotificationUseCase;
import com.xmartin.notification_service.domain.port.in.SendTaskUpdatedNotificationUseCase;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements SendTaskCreatedNotificationUseCase, SendTaskDeletedNotificationUseCase, SendTaskUpdatedNotificationUseCase {

    private final SendTaskCreatedNotificationUseCase sendTaskCreatedNotificationUseCase;
    private final SendTaskDeletedNotificationUseCase sendTaskDeletedNotificationUseCase;
    private final SendTaskUpdatedNotificationUseCase sendTaskUpdatedNotificationUseCase;

    @Override
    public void sendTaskCreatedNotification(CreateTaskEvent createTaskEvent) throws SendEmailException {
        sendTaskCreatedNotificationUseCase.sendTaskCreatedNotification(createTaskEvent);
    }

    @Override
    public void sendTaskDeletedNotification(DeleteTaskEvent deleteTaskEvent) throws SendEmailException {
        sendTaskDeletedNotificationUseCase.sendTaskDeletedNotification(deleteTaskEvent);
    }

    @Override
    public void sendTaskUpdatedNotification(UpdateTaskEvent updateTaskEvent) throws SendEmailException {
        sendTaskUpdatedNotificationUseCase.sendTaskUpdatedNotification(updateTaskEvent);
    }
}

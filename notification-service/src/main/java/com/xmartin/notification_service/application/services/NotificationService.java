package com.xmartin.notification_service.application.services;

import com.xmartin.notification_service.domain.model.*;
import com.xmartin.notification_service.domain.port.in.*;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements SendTaskCreatedNotificationUseCase, SendTaskDeletedNotificationUseCase, SendTaskUpdatedNotificationUseCase, SendPasswordTokenNotificationUseCase, SendResetPasswordNotificationUseCase {

    private final SendTaskCreatedNotificationUseCase sendTaskCreatedNotificationUseCase;
    private final SendTaskDeletedNotificationUseCase sendTaskDeletedNotificationUseCase;
    private final SendTaskUpdatedNotificationUseCase sendTaskUpdatedNotificationUseCase;
    private final SendPasswordTokenNotificationUseCase sendPasswordTokenNotificationUseCase;
    private final SendResetPasswordNotificationUseCase sendResetPasswordNotificationUseCase;

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

    @Override
    public void sendPasswordTokenNotification(PasswordTokenEvent event) throws SendEmailException {
        sendPasswordTokenNotificationUseCase.sendPasswordTokenNotification(event);
    }

    @Override
    public void sendResetPasswordNotificationUseCase(ResetPasswordEvent event) throws SendEmailException {
        sendResetPasswordNotificationUseCase.sendResetPasswordNotificationUseCase(event);
    }
}

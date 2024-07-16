package com.xmartin.notification_service.domain.port.in;

import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface SendTaskUpdatedNotificationUseCase {
    void sendTaskUpdatedNotification(UpdateTaskEvent updateTaskEvent) throws SendEmailException;
}

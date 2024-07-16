package com.xmartin.notification_service.domain.port.in;

import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface SendTaskCreatedNotificationUseCase {
    void sendTaskCreatedNotification(CreateTaskEvent createTaskEvent) throws SendEmailException;
}

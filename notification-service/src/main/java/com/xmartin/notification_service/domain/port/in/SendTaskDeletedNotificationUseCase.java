package com.xmartin.notification_service.domain.port.in;

import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface SendTaskDeletedNotificationUseCase {
    void sendTaskDeletedNotification(DeleteTaskEvent deleteTaskEvent) throws SendEmailException;
}

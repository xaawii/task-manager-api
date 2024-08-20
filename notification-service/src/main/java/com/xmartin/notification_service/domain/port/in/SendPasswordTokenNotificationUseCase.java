package com.xmartin.notification_service.domain.port.in;

import com.xmartin.notification_service.domain.model.PasswordTokenEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface SendPasswordTokenNotificationUseCase {
    void sendPasswordTokenNotification(PasswordTokenEvent event) throws SendEmailException;
}

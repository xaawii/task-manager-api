package com.xmartin.notification_service.domain.port.in;

import com.xmartin.notification_service.domain.model.RegisterUserEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface SendRegisterNotificationUserUseCase {
    void sendRegisterUser(RegisterUserEvent event) throws SendEmailException;
}

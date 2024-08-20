package com.xmartin.notification_service.domain.port.in;

import com.xmartin.notification_service.domain.model.ResetPasswordEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface SendResetPasswordNotificationUseCase {

    void sendResetPasswordNotificationUseCase(ResetPasswordEvent event) throws SendEmailException;
}

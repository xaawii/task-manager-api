package com.xmartin.notification_service.domain.port.out;

import com.xmartin.notification_service.domain.model.*;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface MailSenderPort {

    void sendCreateNotification(CreateTaskEvent createTaskEvent) throws SendEmailException;

    void sendUpdateNotification(UpdateTaskEvent updateTaskEvent) throws SendEmailException;

    void sendDeleteNotification(DeleteTaskEvent deleteTaskEvent) throws SendEmailException;

    void sendPasswordTokenNotification(PasswordTokenEvent event) throws SendEmailException;

    void sendResetPasswordNotification(ResetPasswordEvent event) throws SendEmailException;

    void sendRegisterUserNotification(RegisterUserEvent event) throws SendEmailException;

}

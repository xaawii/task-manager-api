package com.xmartin.notification_service.domain.port.out;

import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface MailSenderPort {

    void sendCreateNotification(CreateTaskEvent createTaskEvent) throws SendEmailException;

    void sendUpdateNotification(UpdateTaskEvent updateTaskEvent) throws SendEmailException;

    void sendDeleteNotification(DeleteTaskEvent deleteTaskEvent) throws SendEmailException;

}

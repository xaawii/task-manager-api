package com.xmartin.notification_service.domain.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

import java.text.ParseException;

public interface TaskEventHandlerPort {
    void createTaskListener(final String json) throws JsonProcessingException, SendEmailException, ParseException;

    void deleteTaskListener(final String json) throws JsonProcessingException, SendEmailException;

    void updateTaskListener(final String json) throws JsonProcessingException, SendEmailException, ParseException;
}

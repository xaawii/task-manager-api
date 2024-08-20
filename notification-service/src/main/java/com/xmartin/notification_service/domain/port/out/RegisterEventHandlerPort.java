package com.xmartin.notification_service.domain.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface RegisterEventHandlerPort {
    void registerUserListener(final String json) throws JsonProcessingException, SendEmailException;
}

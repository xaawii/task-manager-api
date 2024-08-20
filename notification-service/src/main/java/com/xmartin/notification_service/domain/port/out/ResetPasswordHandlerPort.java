package com.xmartin.notification_service.domain.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;

public interface ResetPasswordHandlerPort {
    void passwordTokenListener(final String json) throws JsonProcessingException, SendEmailException;

    void resetPasswordListener(final String json) throws JsonProcessingException, SendEmailException;
}

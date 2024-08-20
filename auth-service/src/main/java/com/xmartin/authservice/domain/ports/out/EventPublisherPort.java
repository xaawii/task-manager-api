package com.xmartin.authservice.domain.ports.out;

import com.xmartin.authservice.domain.model.PasswordTokenEvent;
import com.xmartin.authservice.domain.model.RegisterUserEvent;
import com.xmartin.authservice.domain.model.ResetPasswordEvent;

public interface EventPublisherPort {
    void publish(PasswordTokenEvent passwordTokenEvent);

    void publish(ResetPasswordEvent resetPasswordEvent);
    void publish(RegisterUserEvent registerUserEvent);
}

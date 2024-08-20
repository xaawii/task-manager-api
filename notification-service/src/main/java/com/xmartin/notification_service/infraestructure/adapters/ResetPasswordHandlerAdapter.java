package com.xmartin.notification_service.infraestructure.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.notification_service.application.services.NotificationService;
import com.xmartin.notification_service.domain.model.PasswordTokenEvent;
import com.xmartin.notification_service.domain.model.ResetPasswordEvent;
import com.xmartin.notification_service.domain.port.out.ResetPasswordHandlerPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordHandlerAdapter implements ResetPasswordHandlerPort {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(id = "password-token-listener", topics = "${topics.kafka.password-token}")
    @Override
    public void passwordTokenListener(String json) throws JsonProcessingException, SendEmailException {
        PasswordTokenEvent passwordTokenEvent = objectMapper.readValue(json, PasswordTokenEvent.class);
        notificationService.sendPasswordTokenNotification(passwordTokenEvent);
    }

    @KafkaListener(id = "reset-password-listener", topics = "${topics.kafka.reset-password}")
    @Override
    public void resetPasswordListener(String json) throws JsonProcessingException, SendEmailException {
        ResetPasswordEvent resetPasswordEvent = objectMapper.readValue(json, ResetPasswordEvent.class);
        notificationService.sendResetPasswordNotificationUseCase(resetPasswordEvent);
    }
}

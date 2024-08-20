package com.xmartin.notification_service.infraestructure.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.notification_service.application.services.NotificationService;
import com.xmartin.notification_service.domain.model.RegisterUserEvent;
import com.xmartin.notification_service.domain.port.out.RegisterEventHandlerPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterEventHandlerAdapter implements RegisterEventHandlerPort {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(id = "register-user-listener", topics = "${topics.kafka.register-user}")
    @Override
    public void registerUserListener(String json) throws JsonProcessingException, SendEmailException {
        RegisterUserEvent event = objectMapper.readValue(json, RegisterUserEvent.class);

        notificationService.sendRegisterUser(event);
    }
}

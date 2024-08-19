package com.xmartin.authservice.infraestructure.adapters;

import com.xmartin.authservice.domain.model.PasswordTokenEvent;
import com.xmartin.authservice.domain.model.ResetPasswordEvent;
import com.xmartin.authservice.domain.ports.out.EventPublisherPort;
import com.xmartin.authservice.infraestructure.config.KafkaPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaPropertiesConfig kafkaProperties;


    @Override
    public void publish(PasswordTokenEvent passwordTokenEvent) {
        kafkaTemplate.send(kafkaProperties.getPasswordToken(), passwordTokenEvent);
    }

    @Override
    public void publish(ResetPasswordEvent resetPasswordEvent) {
        kafkaTemplate.send(kafkaProperties.getResetPassword(), resetPasswordEvent);
    }
}

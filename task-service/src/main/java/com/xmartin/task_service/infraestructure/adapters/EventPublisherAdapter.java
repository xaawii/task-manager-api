package com.xmartin.task_service.infraestructure.adapters;

import com.xmartin.task_service.domain.model.CreateTaskEvent;
import com.xmartin.task_service.domain.model.DeleteTaskEvent;
import com.xmartin.task_service.domain.model.UpdateTaskEvent;
import com.xmartin.task_service.domain.port.out.EventPublisherPort;
import com.xmartin.task_service.infraestructure.config.KafkaPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaPropertiesConfig kafkaProperties;

    @Override
    public void publish(CreateTaskEvent createTaskEvent) {
        kafkaTemplate.send(kafkaProperties.getCreateTask(), createTaskEvent);
    }

    @Override
    public void publish(UpdateTaskEvent updateTaskEvent) {
        kafkaTemplate.send(kafkaProperties.getUpdateTask(), updateTaskEvent);
    }

    @Override
    public void publish(DeleteTaskEvent deleteTaskEvent) {
        kafkaTemplate.send(kafkaProperties.getDeleteTask(), deleteTaskEvent);
    }
}

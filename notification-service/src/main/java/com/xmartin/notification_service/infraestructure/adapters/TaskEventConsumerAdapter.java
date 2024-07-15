package com.xmartin.notification_service.infraestructure.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.domain.port.out.TaskEventConsumerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventConsumerAdapter implements TaskEventConsumerPort {

    private final ObjectMapper objectMapper;

    @KafkaListener(id = "create-task-listener", topics = "${topics.kafka.create-task}")
    @Override
    public void createTaskListener(final String json) throws JsonProcessingException {

        CreateTaskEvent createTaskEvent = objectMapper.readValue(json, CreateTaskEvent.class);

        log.info(createTaskEvent.toString());
    }

    @KafkaListener(id = "delete-task-listener", topics = "${topics.kafka.delete-task}")
    @Override
    public void deleteTaskListener(final String json) throws JsonProcessingException {

        DeleteTaskEvent deleteTaskEvent = objectMapper.readValue(json, DeleteTaskEvent.class);

        log.info(deleteTaskEvent.toString());

    }

    @KafkaListener(id = "update-task-listener", topics = "${topics.kafka.update-task}")
    @Override
    public void updateTaskListener(final String json) throws JsonProcessingException {

        UpdateTaskEvent updateTaskEvent = objectMapper.readValue(json, UpdateTaskEvent.class);

        log.info(updateTaskEvent.toString());

    }
}

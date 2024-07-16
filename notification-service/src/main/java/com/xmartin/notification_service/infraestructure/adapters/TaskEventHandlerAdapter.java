package com.xmartin.notification_service.infraestructure.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.notification_service.application.services.NotificationService;
import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.domain.port.out.TaskEventHandlerPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventHandlerAdapter implements TaskEventHandlerPort {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(id = "create-task-listener", topics = "${topics.kafka.create-task}")
    @Override
    public void createTaskListener(final String json) throws JsonProcessingException, SendEmailException {

        CreateTaskEvent createTaskEvent = objectMapper.readValue(json, CreateTaskEvent.class);

        notificationService.sendTaskCreatedNotification(createTaskEvent);
    }

    @KafkaListener(id = "delete-task-listener", topics = "${topics.kafka.delete-task}")
    @Override
    public void deleteTaskListener(final String json) throws JsonProcessingException, SendEmailException {

        DeleteTaskEvent deleteTaskEvent = objectMapper.readValue(json, DeleteTaskEvent.class);

        notificationService.sendTaskDeletedNotification(deleteTaskEvent);

    }

    @KafkaListener(id = "update-task-listener", topics = "${topics.kafka.update-task}")
    @Override
    public void updateTaskListener(final String json) throws JsonProcessingException, SendEmailException {

        UpdateTaskEvent updateTaskEvent = objectMapper.readValue(json, UpdateTaskEvent.class);

        notificationService.sendTaskUpdatedNotification(updateTaskEvent);

    }
}

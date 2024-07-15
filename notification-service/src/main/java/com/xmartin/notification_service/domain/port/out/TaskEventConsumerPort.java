package com.xmartin.notification_service.domain.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;

public interface TaskEventConsumerPort {
    void createTaskListener(final String json) throws JsonProcessingException;

    void deleteTaskListener(final String json) throws JsonProcessingException;

    void updateTaskListener(final String json) throws JsonProcessingException;
}

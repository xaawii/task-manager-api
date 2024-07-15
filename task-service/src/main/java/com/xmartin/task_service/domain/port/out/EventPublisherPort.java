package com.xmartin.task_service.domain.port.out;

import com.xmartin.task_service.domain.model.CreateTaskEvent;
import com.xmartin.task_service.domain.model.DeleteTaskEvent;
import com.xmartin.task_service.domain.model.UpdateTaskEvent;

public interface EventPublisherPort {
    void publish(CreateTaskEvent createTaskEvent);

    void publish(UpdateTaskEvent updateTaskEvent);

    void publish(DeleteTaskEvent deleteTaskEvent);
}

package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;

public interface GetTaskUseCase {
    TaskModel getTaskById(Long id) throws TaskNotFoundException;
}

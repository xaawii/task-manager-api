package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;

public interface UpdateTaskUseCase {
    TaskModel updateTask(TaskModel taskModel) throws TaskNotFoundException;
}

package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.model.TaskModel;

public interface CreateTaskUseCase {
    TaskModel createTask(TaskModel taskModel);
}

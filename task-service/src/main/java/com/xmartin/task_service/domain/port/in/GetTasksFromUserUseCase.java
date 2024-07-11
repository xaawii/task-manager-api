package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.model.TaskModel;

import java.util.List;

public interface GetTasksFromUserUseCase {
    List<TaskModel> getTasksByUserId(Long id);
}

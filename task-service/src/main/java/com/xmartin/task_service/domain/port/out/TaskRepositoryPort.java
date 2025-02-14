package com.xmartin.task_service.domain.port.out;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;

import java.util.List;

public interface TaskRepositoryPort {
    void deleteTask(Long id) throws TaskNotFoundException;

    void deleteAllTasksByUserId(Integer userId);

    void deleteTasksByIdInBatch(List<Long> ids);

    TaskModel saveTask(TaskModel taskModel);

    TaskModel getTaskById(Long id) throws TaskNotFoundException;

    List<TaskModel> getTasksByUserId(Long id);

    List<TaskModel> getTasksByIdInBatch(List<Long> ids);

}

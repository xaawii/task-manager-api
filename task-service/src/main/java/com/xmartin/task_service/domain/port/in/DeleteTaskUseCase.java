package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;

public interface DeleteTaskUseCase {
    void deleteTask(Long id) throws TaskNotFoundException;
}

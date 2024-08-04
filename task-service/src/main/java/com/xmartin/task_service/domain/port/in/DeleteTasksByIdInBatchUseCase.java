package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.exceptions.UserNotFoundException;

import java.util.List;

public interface DeleteTasksByIdInBatchUseCase {

    void deleteTaskByIdInBatch(List<Long> ids) throws TaskNotFoundException, UserNotFoundException;
}

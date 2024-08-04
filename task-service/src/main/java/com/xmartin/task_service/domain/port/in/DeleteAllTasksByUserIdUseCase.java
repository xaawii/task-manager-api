package com.xmartin.task_service.domain.port.in;

import com.xmartin.task_service.domain.exceptions.UserNotFoundException;

public interface DeleteAllTasksByUserIdUseCase {
    void deleteTasksByUserId(Integer userId) throws UserNotFoundException;
}

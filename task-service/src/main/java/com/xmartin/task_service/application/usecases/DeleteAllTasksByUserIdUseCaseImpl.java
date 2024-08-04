package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.UserNotFoundException;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.in.DeleteAllTasksByUserIdUseCase;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteAllTasksByUserIdUseCaseImpl implements DeleteAllTasksByUserIdUseCase {
    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;

    @Transactional
    @Override
    public void deleteTasksByUserId(Integer userId) throws UserNotFoundException {
        UserModel user = userClientRepositoryPort.getUserById(userId);
        if (user == null) throw new UserNotFoundException();

        taskRepositoryPort.deleteAllTasksByUserId(userId);
    }
}

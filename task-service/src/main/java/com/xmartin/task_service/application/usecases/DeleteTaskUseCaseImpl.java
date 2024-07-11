package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.port.in.DeleteTaskUseCase;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        taskRepositoryPort.deleteTask(id);
    }
}

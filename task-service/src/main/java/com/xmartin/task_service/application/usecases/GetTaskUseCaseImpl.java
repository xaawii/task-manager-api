package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.in.GetTaskUseCase;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTaskUseCaseImpl implements GetTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public TaskModel getTaskById(Long id) throws TaskNotFoundException {
        return taskRepositoryPort.getTaskById(id);
    }
}

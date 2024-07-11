package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.in.GetTasksFromUserUseCase;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTasksFromUserUseCaseImpl implements GetTasksFromUserUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public List<TaskModel> getTasksByUserId(Long id) {

        return taskRepositoryPort.getTasksByUserId(id);

    }
}

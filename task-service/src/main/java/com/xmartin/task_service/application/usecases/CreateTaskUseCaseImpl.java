package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.in.CreateTaskUseCase;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCaseImpl implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public TaskModel createTask(TaskModel taskModel) {
        taskModel.setCreateDate(Timestamp.from(Instant.now()));
        taskModel.setUpdateDate(Timestamp.from(Instant.now()));
        return taskRepositoryPort.saveTask(taskModel);
    }
}

package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.in.UpdateTaskUseCase;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdateTaskUseCaseImpl implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public TaskModel updateTask(TaskModel taskModel) throws TaskNotFoundException {

        TaskModel bbddTaskModel = taskRepositoryPort.getTaskById(taskModel.getId());

        taskModel.setUpdateDate(Timestamp.from(Instant.now()));
        taskModel.setCreateDate(bbddTaskModel.getCreateDate());
        taskModel.setUserId(bbddTaskModel.getUserId());

        return taskRepositoryPort.saveTask(taskModel);
    }
}
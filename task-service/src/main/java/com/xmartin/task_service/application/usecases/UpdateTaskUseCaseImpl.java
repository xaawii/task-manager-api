package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.model.UpdateTaskEvent;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.in.UpdateTaskUseCase;
import com.xmartin.task_service.domain.port.out.EventPublisherPort;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UpdateTaskUseCaseImpl implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public TaskModel updateTask(TaskModel taskModel) throws TaskNotFoundException {

        TaskModel bbddTaskModel = taskRepositoryPort.getTaskById(taskModel.getId());

        taskModel.setUpdateDate(LocalDateTime.now());
        taskModel.setCreateDate(bbddTaskModel.getCreateDate());
        taskModel.setUserId(bbddTaskModel.getUserId());

        TaskModel savedTask = taskRepositoryPort.saveTask(taskModel);
        UserModel user = userClientRepositoryPort.getUserById(savedTask.getUserId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UpdateTaskEvent updateTaskEvent = new UpdateTaskEvent(savedTask.getId(),
                savedTask.getTitle(), savedTask.getDescription(), savedTask.getCreateDate().format(formatter),
                savedTask.getUpdateDate().format(formatter), savedTask.getDueDate().format(formatter),
                savedTask.getStatus(), user);

        eventPublisherPort.publish(updateTaskEvent);

        return savedTask;
    }
}
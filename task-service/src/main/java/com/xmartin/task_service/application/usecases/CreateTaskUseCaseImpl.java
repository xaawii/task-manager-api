package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.model.CreateTaskEvent;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.in.CreateTaskUseCase;
import com.xmartin.task_service.domain.port.out.EventPublisherPort;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCaseImpl implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public TaskModel createTask(TaskModel taskModel) {

        taskModel.setCreateDate(LocalDateTime.now());
        taskModel.setUpdateDate(LocalDateTime.now());
        TaskModel savedTask = taskRepositoryPort.saveTask(taskModel);

        UserModel user = userClientRepositoryPort.getUserById(savedTask.getUserId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        CreateTaskEvent createTaskEvent = new CreateTaskEvent(savedTask.getId(),
                savedTask.getTitle(), savedTask.getDescription(), savedTask.getCreateDate().format(formatter),
                savedTask.getCreateDate().format(formatter), savedTask.getCreateDate().format(formatter), savedTask.getStatus(), user);
        eventPublisherPort.publish(createTaskEvent);

        return savedTask;
    }
}

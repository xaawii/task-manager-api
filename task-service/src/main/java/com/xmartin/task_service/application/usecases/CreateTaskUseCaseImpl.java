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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCaseImpl implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public TaskModel createTask(TaskModel taskModel) {

        taskModel.setCreateDate(Timestamp.from(Instant.now()));
        taskModel.setUpdateDate(Timestamp.from(Instant.now()));
        TaskModel savedTask = taskRepositoryPort.saveTask(taskModel);

        UserModel user = userClientRepositoryPort.getUserById(savedTask.getUserId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


        CreateTaskEvent createTaskEvent = new CreateTaskEvent(savedTask.getId(),
                savedTask.getTitle(), savedTask.getDescription(), dateFormat.format(savedTask.getCreateDate()),
                dateFormat.format(savedTask.getCreateDate()), dateFormat.format(savedTask.getCreateDate()), savedTask.getStatus(), user);
        eventPublisherPort.publish(createTaskEvent);

        return savedTask;
    }
}

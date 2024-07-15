package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.DeleteTaskEvent;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.in.DeleteTaskUseCase;
import com.xmartin.task_service.domain.port.out.EventPublisherPort;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {

        TaskModel savedTask = taskRepositoryPort.getTaskById(id);
        taskRepositoryPort.deleteTask(id);

        UserModel user = userClientRepositoryPort.getUserById(savedTask.getUserId());
        DeleteTaskEvent deleteTaskEvent = new DeleteTaskEvent(savedTask.getId(),
                savedTask.getTitle(), savedTask.getDescription(), savedTask.getCreateDate(),
                savedTask.getUpdateDate(), savedTask.getDueDate(), savedTask.getStatus(), user);

        eventPublisherPort.publish(deleteTaskEvent);
    }
}

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
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Transactional
    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {

        TaskModel savedTask = taskRepositoryPort.getTaskById(id);
        taskRepositoryPort.deleteTask(id);

        UserModel user = userClientRepositoryPort.getUserById(savedTask.getUserId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        DeleteTaskEvent deleteTaskEvent = new DeleteTaskEvent(savedTask.getId(),
                savedTask.getTitle(), savedTask.getDescription(), savedTask.getCreateDate().format(formatter),
                savedTask.getUpdateDate().format(formatter), savedTask.getDueDate().format(formatter), savedTask.getStatus(), user);

        eventPublisherPort.publish(deleteTaskEvent);
    }
}

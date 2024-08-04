package com.xmartin.task_service.application.usecases;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.exceptions.UserNotFoundException;
import com.xmartin.task_service.domain.model.DeleteTaskEvent;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.in.DeleteTasksByIdInBatchUseCase;
import com.xmartin.task_service.domain.port.out.EventPublisherPort;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteTasksByIdInBatchUseCaseImpl implements DeleteTasksByIdInBatchUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserClientRepositoryPort userClientRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public void deleteTaskByIdInBatch(List<Long> ids) throws TaskNotFoundException, UserNotFoundException {

        List<TaskModel> savedTasks = taskRepositoryPort.getTasksByIdInBatch(ids);
        if (savedTasks.isEmpty()) throw new TaskNotFoundException();

        UserModel user = userClientRepositoryPort.getUserById(savedTasks.get(0).getUserId());

        if (user == null) throw new UserNotFoundException();

        taskRepositoryPort.deleteTasksByIdInBatch(ids);


        savedTasks.forEach(savedTask -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            DeleteTaskEvent deleteTaskEvent = new DeleteTaskEvent(savedTask.getId(),
                    savedTask.getTitle(), savedTask.getDescription(), savedTask.getCreateDate().format(formatter),
                    savedTask.getUpdateDate().format(formatter), savedTask.getDueDate().format(formatter), savedTask.getStatus(), user);

            eventPublisherPort.publish(deleteTaskEvent);
        });


    }
}

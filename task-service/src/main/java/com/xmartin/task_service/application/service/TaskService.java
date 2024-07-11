package com.xmartin.task_service.application.service;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.in.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements CreateTaskUseCase, DeleteTaskUseCase, GetTaskUseCase, GetTasksFromUserUseCase, UpdateTaskUseCase {

    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final GetTasksFromUserUseCase getTasksFromUserUseCase;

    @Override
    public TaskModel createTask(TaskModel taskModel) {
        return createTaskUseCase.createTask(taskModel);
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        deleteTaskUseCase.deleteTask(id);
    }

    @Override
    public TaskModel getTaskById(Long id) throws TaskNotFoundException {
        return getTaskUseCase.getTaskById(id);
    }

    @Override
    public List<TaskModel> getTasksByUserId(Long id) {
        return getTasksFromUserUseCase.getTasksByUserId(id);
    }

    @Override
    public TaskModel updateTask(TaskModel taskModel) throws TaskNotFoundException {
        return updateTaskUseCase.updateTask(taskModel);
    }
}

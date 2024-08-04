package com.xmartin.task_service.infraestructure.adapters;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.infraestructure.entity.converters.TaskConverter;
import com.xmartin.task_service.infraestructure.repository.JpaTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository jpaTaskRepository;
    private final TaskConverter taskConverter;

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        if (!jpaTaskRepository.existsById(id)) throw new TaskNotFoundException();
        jpaTaskRepository.deleteById(id);
    }

    @Override
    public void deleteAllTasksByUserId(Integer userId) {
        jpaTaskRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteTasksByIdInBatch(List<Long> ids) {
        jpaTaskRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public TaskModel saveTask(TaskModel taskModel) {
        return taskConverter.toModel(jpaTaskRepository.save(taskConverter.toEntity(taskModel)));
    }

    @Override
    public TaskModel getTaskById(Long id) throws TaskNotFoundException {
        return taskConverter.toModel(jpaTaskRepository.findById(id).orElseThrow(TaskNotFoundException::new));
    }

    @Override
    public List<TaskModel> getTasksByUserId(Long id) {
        return taskConverter.toModelList(jpaTaskRepository.findTasksByUserId(id));
    }

    @Override
    public List<TaskModel> getTasksByIdInBatch(List<Long> ids) {
        return taskConverter.toModelList(jpaTaskRepository.findAllById(ids));
    }
}

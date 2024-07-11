package com.xmartin.task_service.infraestructure.adapters;

import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.domain.port.out.TaskRepositoryPort;
import com.xmartin.task_service.infraestructure.entity.converters.TaskConverter;
import com.xmartin.task_service.infraestructure.repository.JpaTaskRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRespository jpaTaskRespository;
    private final TaskConverter taskConverter;

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        if (!jpaTaskRespository.existsById(id)) throw new TaskNotFoundException();
        jpaTaskRespository.deleteById(id);
    }

    @Override
    public TaskModel saveTask(TaskModel taskModel) {
        return taskConverter.toModel(jpaTaskRespository.save(taskConverter.toEntity(taskModel)));
    }

    @Override
    public TaskModel getTaskById(Long id) throws TaskNotFoundException {
        return taskConverter.toModel(jpaTaskRespository.findById(id).orElseThrow(TaskNotFoundException::new));
    }

    @Override
    public List<TaskModel> getTasksByUserId(Long id) {
        return taskConverter.toModelList(jpaTaskRespository.findTasksByUserId(id));
    }
}

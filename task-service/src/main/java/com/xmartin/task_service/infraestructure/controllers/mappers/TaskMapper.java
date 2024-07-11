package com.xmartin.task_service.infraestructure.controllers.mappers;

import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.controllers.dto.CreateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.dto.TaskResponse;
import com.xmartin.task_service.infraestructure.controllers.dto.UpdateTaskRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {

    public TaskModel toModel(CreateTaskRequest createTaskRequest, Long userId) {
        return TaskModel.builder()
                .title(createTaskRequest.getTitle())
                .description(createTaskRequest.getDescription())
                .dueDate(createTaskRequest.getDueDate())
                .status(createTaskRequest.getStatus())
                .userId(userId)
                .build();
    }

    public TaskModel toModel(UpdateTaskRequest updateTaskRequest, Long id) {
        return TaskModel.builder()
                .id(id)
                .title(updateTaskRequest.getTitle())
                .description(updateTaskRequest.getDescription())
                .dueDate(updateTaskRequest.getDueDate())
                .status(updateTaskRequest.getStatus())
                .build();
    }

    public TaskResponse toResponse(TaskModel taskModel) {
        return TaskResponse.builder()
                .id(taskModel.getId())
                .title(taskModel.getTitle())
                .description(taskModel.getDescription())
                .createDate(taskModel.getCreateDate())
                .updateDate(taskModel.getUpdateDate())
                .dueDate(taskModel.getDueDate())
                .status(taskModel.getStatus())
                .userId(taskModel.getUserId())
                .build();
    }

    public List<TaskResponse> toResponseList(List<TaskModel> taskModelList) {
        return taskModelList.stream().map(this::toResponse).toList();
    }
}

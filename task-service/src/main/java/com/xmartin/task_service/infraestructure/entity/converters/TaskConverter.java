package com.xmartin.task_service.infraestructure.entity.converters;

import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskConverter {

    public TaskModel toModel(TaskEntity taskEntity) {
        return TaskModel.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .createDate(taskEntity.getCreateDate())
                .updateDate(taskEntity.getUpdateDate())
                .dueDate(taskEntity.getDueDate())
                .status(taskEntity.getStatus())
                .userId(taskEntity.getUserId())
                .build();
    }

    public TaskEntity toEntity(TaskModel taskModel) {
        return TaskEntity.builder()
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

    public List<TaskModel> toModelList(List<TaskEntity> taskEntityList){
        return taskEntityList.stream().map(this::toModel).toList();
    }
}

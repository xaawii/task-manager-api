package com.xmartin.task_service.infraestructure.controllers.dto;

import com.xmartin.task_service.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Status status;

}

package com.xmartin.task_service.infraestructure.controllers.dto;

import com.xmartin.task_service.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    private String title;
    private String description;
    private Date dueDate;
    private Status status;

}

package com.xmartin.task_service.domain.model;

import com.xmartin.task_service.domain.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskModel {

    private Long id;
    private String title;
    private String description;
    private Date createDate;
    private Date updateDate;
    private Date dueDate;
    private Status status;
    private Long userId;

}

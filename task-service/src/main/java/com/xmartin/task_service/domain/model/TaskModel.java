package com.xmartin.task_service.domain.model;

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
public class TaskModel {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime dueDate;
    private Status status;
    private Integer userId;

}

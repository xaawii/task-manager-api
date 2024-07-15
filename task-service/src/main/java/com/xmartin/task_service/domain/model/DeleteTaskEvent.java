package com.xmartin.task_service.domain.model;

import com.xmartin.task_service.domain.enums.Status;

import java.util.Date;

public record DeleteTaskEvent(
        Long id,
        String title,
        String description,
        Date createDate,
        Date updateDate,
        Date dueDate,
        Status status,
        UserModel user
) {
}

package com.xmartin.task_service.domain.model;

import com.xmartin.task_service.domain.enums.Status;

public record CreateTaskEvent(
        Long id,
        String title,
        String description,
        String createDate,
        String updateDate,
        String dueDate,
        Status status,
        UserModel user) {
}

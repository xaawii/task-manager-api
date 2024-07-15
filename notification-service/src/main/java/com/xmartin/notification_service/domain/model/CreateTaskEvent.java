package com.xmartin.notification_service.domain.model;



import com.xmartin.notification_service.domain.enums.Status;

import java.util.Date;

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

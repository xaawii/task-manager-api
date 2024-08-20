package com.xmartin.notification_service.domain.model;

public record ResetPasswordEvent(
        UserModel user
) {
}

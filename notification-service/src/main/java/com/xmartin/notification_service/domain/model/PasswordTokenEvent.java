package com.xmartin.notification_service.domain.model;

public record PasswordTokenEvent(
        Long id,

        String token,

        String email
) {
}

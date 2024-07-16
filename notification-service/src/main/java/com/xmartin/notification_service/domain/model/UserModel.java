package com.xmartin.notification_service.domain.model;

public record UserModel(
        Integer id,
        String name,
        String email,
        String role
) {

}

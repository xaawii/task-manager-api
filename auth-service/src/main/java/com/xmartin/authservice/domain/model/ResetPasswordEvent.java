package com.xmartin.authservice.domain.model;

public record ResetPasswordEvent(
        UserModel user
) {
}

package com.xmartin.authservice.domain.model;

public record PasswordTokenEvent(
        Long id,

        String token,

        String email
) {
}

package com.xmartin.authservice.application.service;

import com.xmartin.authservice.domain.model.PasswordTokenModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PasswordTokenProvider {

    @Value("${password-token.expiration-min}")
    private Long expirationmin;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();


    public PasswordTokenModel generatePasswordToken(String email) {
        return PasswordTokenModel.builder()
                .token(generateRecoveryCode())
                .email(email)
                .expiryDate(LocalDateTime.now().plusMinutes(expirationmin))
                .build();
    }

    public boolean isTokenExpired(LocalDateTime expiryDate) {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    private static String generateRecoveryCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}

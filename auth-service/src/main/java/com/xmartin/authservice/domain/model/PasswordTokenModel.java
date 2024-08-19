package com.xmartin.authservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordTokenModel {

    private Long id;

    private String token;

    private String email;

    private LocalDateTime expiryDate;

}

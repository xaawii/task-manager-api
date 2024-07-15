package com.xmartin.authservice.infraestructure.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginDto {
    @Email(message = "It's not a valid email format.")
    private String email;
    private String password;
}

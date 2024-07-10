package com.xmartin.userservice.infraestructure.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String role;
}

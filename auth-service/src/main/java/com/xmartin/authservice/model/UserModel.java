package com.xmartin.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserModel {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
}

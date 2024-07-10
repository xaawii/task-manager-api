package com.xmartin.userservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserModel {


    private Integer id;
    private String name;
    private String email;
    private String password;
    private String role;


}

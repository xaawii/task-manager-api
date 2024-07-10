package com.xmartin.userservice.infraestructure.controller.mappers;


import com.xmartin.userservice.infraestructure.controller.dtos.UserRequest;
import com.xmartin.userservice.infraestructure.controller.dtos.UserResponse;
import com.xmartin.userservice.domain.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(UserModel user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }

    public UserModel toModel(UserRequest user) {
        return UserModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }
}

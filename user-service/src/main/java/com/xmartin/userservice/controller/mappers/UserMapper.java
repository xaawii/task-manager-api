package com.xmartin.userservice.controller.mappers;


import com.xmartin.userservice.controller.dtos.UserRequest;
import com.xmartin.userservice.controller.dtos.UserResponse;
import com.xmartin.userservice.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }

    public User toModel(UserRequest user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }
}

package com.xmartin.userservice.service.converters;


import com.xmartin.userservice.domain.User;
import com.xmartin.userservice.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toModel(UserEntity userEntity) {

        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .role(userEntity.getRole())
                .build();

    }

    public UserEntity toEntity(User user) {

        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRole())
                .build();

    }
}

package com.xmartin.userservice.infraestructure.converters;


import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.infraestructure.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserModel toModel(UserEntity userEntity) {

        return UserModel.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .role(userEntity.getRole())
                .build();

    }

    public UserEntity toEntity(UserModel user) {

        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRole())
                .build();

    }
}

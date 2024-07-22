package com.xmartin.userservice.infraestructure.converters;

import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.infraestructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {
    @InjectMocks
    private UserConverter userConverter;

    @Test
    void toModel() {
        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .email("xavi@test.com")
                .role("ROLE_USER")
                .password("password")
                .name("Xavi")
                .build();

        UserModel userModel = userConverter.toModel(userEntity);

        assertEquals(userEntity.getId(), userModel.getId());
        assertEquals(userEntity.getEmail(), userModel.getEmail());
        assertEquals(userEntity.getRole(), userModel.getRole());
        assertEquals(userEntity.getPassword(), userModel.getPassword());
        assertEquals(userEntity.getName(), userModel.getName());
    }

    @Test
    void toEntity() {
        UserModel userModel = UserModel.builder()
                .id(1)
                .email("xavi@test.com")
                .role("ROLE_USER")
                .password("password")
                .name("Xavi")
                .build();

        UserEntity userEntity = userConverter.toEntity(userModel);

        assertEquals(userModel.getId(), userEntity.getId());
        assertEquals(userModel.getEmail(), userEntity.getEmail());
        assertEquals(userModel.getRole(), userEntity.getRole());
        assertEquals(userModel.getPassword(), userEntity.getPassword());
        assertEquals(userModel.getName(), userEntity.getName());
    }
}
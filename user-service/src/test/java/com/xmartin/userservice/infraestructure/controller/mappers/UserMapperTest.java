package com.xmartin.userservice.infraestructure.controller.mappers;

import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.infraestructure.controller.dtos.UserRequest;
import com.xmartin.userservice.infraestructure.controller.dtos.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void toResponse() {
        UserModel userModel = UserModel.builder()
                .id(1)
                .email("xavi@test.com")
                .role("ROLE_USER")
                .password("password")
                .name("Xavi")
                .build();

        UserResponse userResponse = userMapper.toResponse(userModel);

        assertEquals(userModel.getId(), userResponse.getId());
        assertEquals(userModel.getEmail(), userResponse.getEmail());
        assertEquals(userModel.getRole(), userResponse.getRole());
        assertEquals(userModel.getPassword(), userResponse.getPassword());
        assertEquals(userModel.getName(), userResponse.getName());
    }

    @Test
    void toModel() {
        UserRequest userRequest = UserRequest.builder()
                .id(1)
                .email("xavi@test.com")
                .role("ROLE_USER")
                .password("password")
                .name("Xavi")
                .build();

        UserModel userModel = userMapper.toModel(userRequest);

        assertEquals(userRequest.getId(), userModel.getId());
        assertEquals(userRequest.getEmail(), userModel.getEmail());
        assertEquals(userRequest.getRole(), userModel.getRole());
        assertEquals(userRequest.getPassword(), userModel.getPassword());
        assertEquals(userRequest.getName(), userModel.getName());
    }
}
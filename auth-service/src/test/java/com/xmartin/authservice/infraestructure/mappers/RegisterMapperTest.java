package com.xmartin.authservice.infraestructure.mappers;

import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RegisterMapperTest {
    @InjectMocks
    private RegisterMapper registerMapper;

    @Test
    void registerDtoToModel() {
        RegisterDto registerDto = RegisterDto.builder()
                .name("Xavi")
                .email("xavi@test.com")
                .password("password")
                .build();

        RegisterModel registerModel = registerMapper.registerDtoToModel(registerDto);

        assertEquals(registerDto.getName(), registerModel.getName());
        assertEquals(registerDto.getEmail(), registerModel.getEmail());
        assertEquals(registerDto.getPassword(), registerModel.getPassword());
    }

}
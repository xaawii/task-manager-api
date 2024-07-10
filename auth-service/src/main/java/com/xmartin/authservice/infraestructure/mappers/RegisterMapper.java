package com.xmartin.authservice.infraestructure.mappers;

import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {
    public RegisterModel registerDtoToModel(RegisterDto registerDto) {
        return RegisterModel.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .build();
    }
}

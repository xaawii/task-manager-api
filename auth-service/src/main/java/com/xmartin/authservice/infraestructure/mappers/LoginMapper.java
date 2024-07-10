package com.xmartin.authservice.infraestructure.mappers;

import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginModel loginDtoToModel(LoginDto loginDto) {
        return LoginModel.builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword())
                .build();
    }
}

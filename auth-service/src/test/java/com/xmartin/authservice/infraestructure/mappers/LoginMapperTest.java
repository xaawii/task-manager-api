package com.xmartin.authservice.infraestructure.mappers;

import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LoginMapperTest {
    @InjectMocks
    private LoginMapper loginMapper;


    @Test
    void loginDtoToModel() {
        LoginDto loginDto = LoginDto.builder()
                .email("xavi@test.com")
                .password("password")
                .build();

        LoginModel loginModel = loginMapper.loginDtoToModel(loginDto);

        assertEquals(loginDto.getEmail(), loginModel.getEmail());
        assertEquals(loginDto.getPassword(), loginModel.getPassword());
    }

}
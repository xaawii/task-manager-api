package com.xmartin.authservice.domain.ports.out;

import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;
import com.xmartin.authservice.domain.model.UserModel;

public interface AuthUserServicePort {

    public UserModel save(RegisterDto registerDto);

    public TokenDto login(LoginDto loginDto);

    public TokenDto validate(String token, RequestDto requestDto);
}

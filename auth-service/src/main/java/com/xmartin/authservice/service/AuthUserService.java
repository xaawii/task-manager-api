package com.xmartin.authservice.service;

import com.xmartin.authservice.controller.dto.LoginDto;
import com.xmartin.authservice.controller.dto.RegisterDto;
import com.xmartin.authservice.controller.dto.RequestDto;
import com.xmartin.authservice.controller.dto.TokenDto;
import com.xmartin.authservice.model.UserModel;

public interface AuthUserService {

    public UserModel save(RegisterDto registerDto);

    public TokenDto login(LoginDto loginDto);

    public TokenDto validate(String token, RequestDto requestDto);
}

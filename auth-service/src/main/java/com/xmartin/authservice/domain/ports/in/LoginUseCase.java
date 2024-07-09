package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;

public interface LoginUseCase {

    TokenDto login(LoginDto loginDto);
}

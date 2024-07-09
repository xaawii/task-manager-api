package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;

public interface RegisterUseCase {
    UserModel register(RegisterDto registerDto);
}

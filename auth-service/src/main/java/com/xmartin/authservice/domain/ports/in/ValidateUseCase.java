package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;

public interface ValidateUseCase {
    TokenDto validate(String token, RequestDto requestDto);
}

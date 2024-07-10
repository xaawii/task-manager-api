package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.model.RequestModel;

public interface ValidateUseCase {
    String validate(String token, RequestModel requestModel) throws InvalidTokenException;
}

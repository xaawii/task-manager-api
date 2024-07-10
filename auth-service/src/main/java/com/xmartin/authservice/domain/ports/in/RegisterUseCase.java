package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.UserModel;

public interface RegisterUseCase {
    UserModel register(RegisterModel registerModel) throws EmailAlreadyInUseException;
}

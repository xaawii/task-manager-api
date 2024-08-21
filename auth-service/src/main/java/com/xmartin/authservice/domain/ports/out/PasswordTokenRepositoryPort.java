package com.xmartin.authservice.domain.ports.out;

import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.model.PasswordTokenModel;

public interface PasswordTokenRepositoryPort {

    PasswordTokenModel save(PasswordTokenModel passwordTokenModel);

    PasswordTokenModel findByToken(String token) throws TokenNotFoundException;

    void remove(String token);

    void tokenCleanUp();

}

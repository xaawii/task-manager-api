package com.xmartin.authservice.domain.ports.out;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.model.UserModel;

import java.util.Optional;

public interface UserClientPort {

    UserModel save(UserModel userModel) throws EmailAlreadyInUseException;

    Optional<UserModel> getUserByEmail(String email);

    boolean getUserExistsByEmail(String email);
}

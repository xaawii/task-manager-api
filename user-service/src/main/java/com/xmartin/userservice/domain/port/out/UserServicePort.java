package com.xmartin.userservice.domain.port.out;


import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;


public interface UserServicePort {


    //guarda un nuevo usuario en la bbdd
    UserModel save(UserModel newUser);


    UserModel getUserByEmail(String email) throws UserNotFoundException;

    boolean userExists(String email);

    void deleteUser(String email) throws UserNotFoundException;

}

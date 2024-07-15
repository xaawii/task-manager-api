package com.xmartin.userservice.domain.port.out;


import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;


public interface UserServicePort {


    //guarda un nuevo usuario en la bbdd
    UserModel save(UserModel newUser);


    UserModel getUserByEmail(String email) throws UserNotFoundException;

    UserModel getUserById(Integer id) throws UserNotFoundException;

    boolean userExists(String email);

    void deleteUser(String email) throws UserNotFoundException;

}

package com.xmartin.userservice.service;


import com.xmartin.userservice.domain.User;
import com.xmartin.userservice.exceptions.UserNotFoundException;

public interface UserService {


    //guarda un nuevo usuario en la bbdd
    public User save(User newUser);


    public User getUserByEmail(String email) throws UserNotFoundException;

    public void deleteUser(String email) throws UserNotFoundException;

}

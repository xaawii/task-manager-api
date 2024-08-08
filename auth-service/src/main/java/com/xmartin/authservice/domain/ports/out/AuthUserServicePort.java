package com.xmartin.authservice.domain.ports.out;

import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.model.UserModel;

public interface AuthUserServicePort {

    UserModel save(RegisterModel registerModel) throws EmailAlreadyInUseException;

    String login(LoginModel loginModel) throws WrongPasswordException, UserNotFoundException;

    String validateRequestAndToken(String token, RequestModel requestModel) throws InvalidTokenException;
    String validateToken(String token) throws InvalidTokenException;
}

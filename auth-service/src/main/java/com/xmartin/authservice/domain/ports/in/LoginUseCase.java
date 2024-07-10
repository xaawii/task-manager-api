package com.xmartin.authservice.domain.ports.in;

import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;

public interface LoginUseCase {

    String login(LoginModel loginModel) throws UserNotFoundException, WrongPasswordException;
}

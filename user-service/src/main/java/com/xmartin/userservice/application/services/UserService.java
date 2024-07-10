package com.xmartin.userservice.application.services;

import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.DeleteUserUseCase;
import com.xmartin.userservice.domain.port.in.GetUserUseCase;
import com.xmartin.userservice.domain.port.in.SaveUserUseCase;
import com.xmartin.userservice.domain.port.in.UserExistsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserExistsUseCase, DeleteUserUseCase, GetUserUseCase, SaveUserUseCase {

    private final UserExistsUseCase userExistsUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final SaveUserUseCase saveUserUseCase;

    @Override
    public boolean userExists(String email) {
        return userExistsUseCase.userExists(email);
    }

    @Override
    public void deleteUser(String email) throws UserNotFoundException {
        deleteUserUseCase.deleteUser(email);
    }

    @Override
    public UserModel getUserByEmail(String email) throws UserNotFoundException {
        return getUserUseCase.getUserByEmail(email);
    }

    @Override
    public UserModel saveUser(UserModel user) throws EmailAlreadyInUseException {
        return saveUserUseCase.saveUser(user);
    }
}

package com.xmartin.userservice.application.services;

import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserExistsUseCase, DeleteUserUseCase, GetUserByEmailUseCase, SaveUserUseCase, GetUserByIdUseCase, UpdateUserUseCase {

    private final UserExistsUseCase userExistsUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @Override
    public boolean userExists(String email) {
        return userExistsUseCase.userExists(email);
    }

    @Override
    public void deleteUser(String email) throws UserNotFoundException {
        deleteUserUseCase.deleteUser(email);
    }


    @Override
    public UserModel saveUser(UserModel user) throws EmailAlreadyInUseException {
        return saveUserUseCase.saveUser(user);
    }

    @Override
    public UserModel getUserByEmail(String email) throws UserNotFoundException {
        return getUserByEmailUseCase.getUserByEmail(email);
    }

    @Override
    public UserModel getUserById(Integer userId) throws UserNotFoundException {
        return getUserByIdUseCase.getUserById(userId);
    }

    @Override
    public UserModel updateUser(UserModel userModel, Integer userId) throws UserNotFoundException {
        return updateUserUseCase.updateUser(userModel, userId);
    }
}

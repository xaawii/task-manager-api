package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.port.in.DeleteUserUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserServicePort userServicePort;
    @Override
    public void deleteUser(String email) throws UserNotFoundException {
        userServicePort.deleteUser(email);
    }
}

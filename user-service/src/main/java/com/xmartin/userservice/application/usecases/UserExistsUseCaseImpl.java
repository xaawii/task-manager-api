package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.port.in.UserExistsUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExistsUseCaseImpl implements UserExistsUseCase {

    private final UserServicePort userServicePort;

    public boolean userExists(String email) {
        return userServicePort.userExists(email);
    }
}

package com.xmartin.userservice.application.usecases;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.in.DeleteUserUseCase;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import com.xmartin.userservice.infraestructure.client.TaskClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserServicePort userServicePort;
    private final TaskClient taskClient;

    @Transactional
    @Override
    public void deleteUser(String email) throws UserNotFoundException {
        UserModel userModel = userServicePort.getUserByEmail(email);
        taskClient.deleteAllTasksByUserId(userModel.getId());
        userServicePort.deleteUser(email);
    }
}

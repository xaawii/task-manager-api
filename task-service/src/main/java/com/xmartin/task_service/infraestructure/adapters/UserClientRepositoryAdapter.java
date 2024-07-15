package com.xmartin.task_service.infraestructure.adapters;

import com.xmartin.task_service.domain.model.UserModel;
import com.xmartin.task_service.domain.port.out.UserClientRepositoryPort;
import com.xmartin.task_service.infraestructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientRepositoryAdapter implements UserClientRepositoryPort {
    private final UserClient userClient;
    @Override
    public UserModel getUserById(Integer id) {
        return userClient.getUserById(id);
    }
}

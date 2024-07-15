package com.xmartin.task_service.domain.port.out;

import com.xmartin.task_service.domain.model.UserModel;

public interface UserClientRepositoryPort {
    UserModel getUserById(Integer id);
}

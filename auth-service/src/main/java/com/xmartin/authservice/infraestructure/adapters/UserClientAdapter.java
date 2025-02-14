package com.xmartin.authservice.infraestructure.adapters;

import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.out.UserClientPort;
import com.xmartin.authservice.infraestructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserClientAdapter implements UserClientPort {

    private final UserClient userClient;

    @Override
    public UserModel save(UserModel userModel) {
        return userClient.saveUser(userModel);
    }

    @Override
    public Optional<UserModel> getUserByEmail(String email) {
        return userClient.getUserByEmail(email);
    }

    @Override
    public boolean getUserExistsByEmail(String email) {
        return userClient.getUserExistsByEmail(email);
    }

    @Override
    public UserModel updateUser(UserModel userModel, Integer userId) {
        return userClient.updateUser(userModel, userId);
    }


}

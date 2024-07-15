package com.xmartin.userservice.infraestructure.adapters;


import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.domain.port.out.UserServicePort;
import com.xmartin.userservice.infraestructure.converters.UserConverter;
import com.xmartin.userservice.infraestructure.entity.UserEntity;
import com.xmartin.userservice.infraestructure.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserServiceAdapter implements UserServicePort {

    //proporciona una instancia de user repository
    private final JpaUserRepository jpaUserRepository;
    private final UserConverter userConverter;


    //guarda un nuevo usuario en la bbdd
    @Override
    public UserModel save(UserModel newUser) {
        return userConverter.toModel(jpaUserRepository.save(userConverter.toEntity(newUser)));
    }


    @Override
    public UserModel getUserByEmail(String email) throws UserNotFoundException {
        return userConverter.toModel(jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email)));
    }

    @Override
    public UserModel getUserById(Integer id) throws UserNotFoundException {
        return userConverter.toModel(jpaUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public boolean userExists(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteUser(String email) throws UserNotFoundException {
        UserEntity userEntity = jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        jpaUserRepository.delete(userEntity);
    }

}

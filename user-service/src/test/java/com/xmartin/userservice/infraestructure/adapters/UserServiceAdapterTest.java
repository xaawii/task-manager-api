package com.xmartin.userservice.infraestructure.adapters;

import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.infraestructure.converters.UserConverter;
import com.xmartin.userservice.infraestructure.entity.UserEntity;
import com.xmartin.userservice.infraestructure.repository.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceAdapterTest {
    @InjectMocks
    private UserServiceAdapter userServiceAdapter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private JpaUserRepository jpaUserRepository;

    private UserModel userModel;
    private UserModel savedUserModel;


    @BeforeEach
    void setUp() {

        userModel = UserModel.builder()
                .name("Xavi")
                .email("xavi@example.com")
                .role("ROLE_USER")
                .build();

        savedUserModel = UserModel.builder()
                .id(1)
                .name("Xavi")
                .email("xavi@example.com")
                .role("ROLE_USER")
                .build();

    }

    @Test
    void save() {
        //Given
        UserEntity userEntity = new UserEntity();
        UserEntity savedUserEntity = new UserEntity();

        when(userConverter.toEntity(userModel)).thenReturn(userEntity);
        when(jpaUserRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userConverter.toModel(savedUserEntity)).thenReturn(savedUserModel);

        //When
        UserModel result = userServiceAdapter.save(userModel);

        //Then
        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getRole(), result.getRole());
        assertEquals(1, result.getId());

        verify(userConverter).toEntity(userModel);
        verify(jpaUserRepository).save(userEntity);
        verify(userConverter).toModel(savedUserEntity);

    }

    @Test
    void getUserByEmail_success() {
        //Given
        String email = "xavi@example.com";
        UserEntity savedUserEntity = new UserEntity();

        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(savedUserEntity));
        when(userConverter.toModel(savedUserEntity)).thenReturn(savedUserModel);

        //When
        UserModel result = assertDoesNotThrow(() -> userServiceAdapter.getUserByEmail(email));

        //Then
        assertEquals(email, result.getEmail());
        assertEquals(1, result.getId());

        verify(jpaUserRepository).findByEmail(email);
        verify(userConverter).toModel(savedUserEntity);
    }

    @Test
    void getUserByEmail_NotFound() {
        //Given
        String email = "xavi@example.com";

        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.empty());


        //When
        assertThrows(UserNotFoundException.class, () -> userServiceAdapter.getUserByEmail(email));

        //Then

        verify(jpaUserRepository).findByEmail(email);
        verify(userConverter, never()).toModel(any(UserEntity.class));
    }

    @Test
    void getUserById_success() {

        //Given
        Integer id = 1;
        UserEntity savedUserEntity = new UserEntity();

        when(jpaUserRepository.findById(id)).thenReturn(Optional.of(savedUserEntity));
        when(userConverter.toModel(savedUserEntity)).thenReturn(savedUserModel);

        //When
        UserModel result = assertDoesNotThrow(() -> userServiceAdapter.getUserById(id));

        //Then
        assertEquals(id, result.getId());
        verify(jpaUserRepository).findById(id);
        verify(userConverter).toModel(savedUserEntity);
    }

    @Test
    void getUserById_NotFound() {

        //Given
        Integer id = 1;
        UserEntity savedUserEntity = new UserEntity();

        when(jpaUserRepository.findById(id)).thenReturn(Optional.empty());

        //When
        assertThrows(UserNotFoundException.class, () -> userServiceAdapter.getUserById(id));

        //Then
        verify(jpaUserRepository).findById(id);
        verify(userConverter, never()).toModel(savedUserEntity);
    }

    @Test
    void userExists_true() {
        //Given
        String email = "xavi@example.com";
        when(jpaUserRepository.existsByEmail(email)).thenReturn(true);

        //When
        boolean response = userServiceAdapter.userExists(email);

        //Then
        assertTrue(response);
        verify(jpaUserRepository).existsByEmail(email);
    }

    @Test
    void userExists_false() {
        //Given
        String email = "xavi@example.com";
        when(jpaUserRepository.existsByEmail(email)).thenReturn(false);

        //When
        boolean response = userServiceAdapter.userExists(email);

        //Then
        assertFalse(response);
        verify(jpaUserRepository).existsByEmail(email);
    }

    @Test
    void deleteUser_success() {
        //Given
        String email = "xavi@example.com";
        UserEntity savedUserEntity = new UserEntity();

        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(savedUserEntity));

        //When
        assertDoesNotThrow(() -> userServiceAdapter.deleteUser(email));

        //Then
        verify(jpaUserRepository).delete(savedUserEntity);
    }

    @Test
    void deleteUser_NotFound() {
        //Given
        String email = "xavi@example.com";
        UserEntity savedUserEntity = new UserEntity();

        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        //When
        assertThrows(UserNotFoundException.class, () -> userServiceAdapter.deleteUser(email));

        //Then
        verify(jpaUserRepository, never()).delete(savedUserEntity);
    }
}
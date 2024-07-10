package com.xmartin.userservice.domain.port.in;

public interface UserExistsUseCase {
    boolean userExists(String email);
}

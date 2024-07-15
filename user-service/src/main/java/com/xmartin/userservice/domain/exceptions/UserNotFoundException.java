package com.xmartin.userservice.domain.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found.");
    }

    public UserNotFoundException(Integer id) {
        super("User with ID " + id + " not found.");
    }
}

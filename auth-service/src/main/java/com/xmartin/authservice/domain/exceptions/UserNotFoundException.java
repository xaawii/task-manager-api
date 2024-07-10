package com.xmartin.authservice.domain.exceptions;

import lombok.NoArgsConstructor;


public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found.");
    }
}

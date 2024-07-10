package com.xmartin.userservice.domain.exceptions;

public class EmailAlreadyInUseException extends Exception {
    public EmailAlreadyInUseException(String email) {
        super("Email " + email + " already in use.");
    }
}

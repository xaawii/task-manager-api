package com.xmartin.authservice.domain.exceptions;

public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
        super("Email or password don't match.");
    }
}

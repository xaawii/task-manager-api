package com.xmartin.authservice.domain.exceptions;

public class TokenNotFoundException extends Exception {
    public TokenNotFoundException() {
        super("Password token not found");
    }
}

package com.xmartin.authservice.domain.exceptions;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(){
        super("Invalid token.");
    }
}

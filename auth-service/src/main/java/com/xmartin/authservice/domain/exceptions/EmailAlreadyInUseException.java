package com.xmartin.authservice.domain.exceptions;

public class EmailAlreadyInUseException extends Exception{
    public EmailAlreadyInUseException(String message){
        super(message);
    }
}

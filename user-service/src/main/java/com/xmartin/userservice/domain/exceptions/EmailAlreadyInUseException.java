package com.xmartin.userservice.domain.exceptions;

public class EmailAlreadyInUseException extends Exception{
    public EmailAlreadyInUseException(String message){
        super(message);
    }
}

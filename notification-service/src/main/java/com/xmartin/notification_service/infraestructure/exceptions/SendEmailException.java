package com.xmartin.notification_service.infraestructure.exceptions;

public class SendEmailException extends Exception {
    public SendEmailException(Exception e) {
        super("Error while sending email: " + e);
    }
}

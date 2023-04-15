package com.example.accountverification.exceptions;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

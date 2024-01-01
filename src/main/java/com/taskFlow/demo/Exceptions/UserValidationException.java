package com.taskFlow.demo.Exceptions;

public class UserValidationException extends  RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}

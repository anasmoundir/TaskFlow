package com.taskFlow.demo.Exceptions;

public class UserNotFoundException extends  RuntimeException{
    public UserNotFoundException(String message) {
            super(message);
    }

}

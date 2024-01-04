package com.taskFlow.demo.Exceptions;

import org.apache.logging.log4j.message.StringFormattedMessage;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException(String message) {
        super(message);
    }
}

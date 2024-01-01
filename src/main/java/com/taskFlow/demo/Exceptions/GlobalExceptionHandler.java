package com.taskFlow.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundEception(final TaskNotFoundException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<String> handleUserALreadyExist(final UserValidationException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(DateException.class)
    public  ResponseEntity<String>  handeDateisBeforException(final DateException e)
    {
        return  new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(UserNotFoundException.class)
     public  ResponseEntity<String> handleUserNotFoundException(final  UserNotFoundException e)
    {
        return  new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}

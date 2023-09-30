package com.Wildlife.Recording.model.exceptions;

public class UserCreationException extends RuntimeException{
    public UserCreationException(String message){
        super(message);
    }
}

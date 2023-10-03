package com.Wildlife.Recording.model.exceptions;

public class PasswordHashNotFoundException extends RuntimeException{
    public PasswordHashNotFoundException(String message){
        super(message);
    }
}

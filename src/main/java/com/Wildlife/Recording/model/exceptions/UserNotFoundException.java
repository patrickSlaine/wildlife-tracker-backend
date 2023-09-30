package com.Wildlife.Recording.model.exceptions;

import com.Wildlife.Recording.model.entities.User;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}

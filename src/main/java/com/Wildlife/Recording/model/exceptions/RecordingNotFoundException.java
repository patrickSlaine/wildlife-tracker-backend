package com.Wildlife.Recording.model.exceptions;

public class RecordingNotFoundException extends RuntimeException{
    public RecordingNotFoundException(String message){
        super(message);
    }
}

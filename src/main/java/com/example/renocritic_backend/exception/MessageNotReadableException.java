package com.example.renocritic_backend.exception;

public class MessageNotReadableException extends RuntimeException {
    public MessageNotReadableException(){
        super("Invalid Data. Please try again.");
    }
}

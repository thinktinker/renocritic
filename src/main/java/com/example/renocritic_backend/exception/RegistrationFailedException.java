package com.example.renocritic_backend.exception;

public class RegistrationFailedException extends RuntimeException{
    public RegistrationFailedException(String message){
        super(String.format("%s Please try again.", message));
    }
}

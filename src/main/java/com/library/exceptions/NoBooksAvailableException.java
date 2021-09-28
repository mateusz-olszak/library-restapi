package com.library.exceptions;

public class NoBooksAvailableException extends RuntimeException{
    public NoBooksAvailableException(String message) {
        super(message);
    }
}

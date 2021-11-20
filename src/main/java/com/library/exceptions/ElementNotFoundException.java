package com.library.exceptions;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException() {
        super("Could not find such element with given id");
    }
}

package com.library.status;

public enum Status {
    AVAILABLE("Available"), RENTED("Rented"), DESTROYED("Destroyed"), LOST("Lost");

    private String status;

    Status(String status) {
        this.status = status;
    }
}

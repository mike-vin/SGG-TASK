package com.sgg.test.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}

package com.sgg.test.exceptions;

public class TypeNotSupportedException extends RuntimeException {
    public TypeNotSupportedException(String message) {
        super(message);
    }
}

package com.example.demo.exceptions;

public class ConflictDataException extends RuntimeException{
    public ConflictDataException(String message) {
        super(message);
    }
}

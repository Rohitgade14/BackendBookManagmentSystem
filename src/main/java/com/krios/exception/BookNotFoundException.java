package com.krios.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("Book not found on the server.");
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}

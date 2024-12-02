package com.krios.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle BookNotFoundException
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiResponse> handleBookNotFoundException(BookNotFoundException ex) {
        String message = ex.getMessage();
        logger.error("Book not found: {}", message); // Log the error
        ApiResponse apiResponse = ApiResponse.builder()
                .message(message)
                .success(false)  // Set to false to indicate an error
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = "Invalid input provided: " + ex.getMessage();
        logger.warn("Illegal argument: {}", message); // Log a warning
        ApiResponse apiResponse = ApiResponse.builder()
                .message(message)
                .success(false)  // Set to false to indicate an error
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex); // Log the error with stack trace
        ApiResponse apiResponse = ApiResponse.builder()
                .message("An unexpected error occurred")
                .success(false)  // Set to false to indicate an error
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

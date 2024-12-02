package com.krios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krios.dto.BookDto;
import com.krios.exception.BookNotFoundException;
import com.krios.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/book")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    @Autowired
    private BookService bookService;

    // Get all books
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Integer id) {
        BookDto bookDto = bookService.getBookById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
        return ResponseEntity.ok(bookDto);
    }

    // Save a single book with validation
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(null);
        }
        BookDto savedBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // Save multiple books with validation
    @PostMapping("/bulk")
    public ResponseEntity<List<BookDto>> createBooks(@Valid @RequestBody List<BookDto> bookDtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(null);
        }
        List<BookDto> savedBooks = bookService.createBooks(bookDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooks);
    }

    // Update a book by ID with validation
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Integer id, @Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(null);
        }
        BookDto updatedBook = bookService.updateBook(id, bookDto)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
        return ResponseEntity.ok(updatedBook);
    }

    // Delete a book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.getBookById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
        
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

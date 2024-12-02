package com.krios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.krios.dto.BookDto;
public interface BookService {

    List<BookDto> getAllBooks(); 

    Optional<BookDto> getBookById(Integer id); 

    BookDto createBook(BookDto bookDto); 

    List<BookDto> createBooks(List<BookDto> bookDtos); 

    Optional<BookDto> updateBook(Integer id, BookDto bookDetails); 

    BookDto deleteBook(Integer id); 

    Page<BookDto> getBooksByTitle(String title, Pageable pageable);
}

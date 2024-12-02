package com.krios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krios.dto.BookDto; 
import com.krios.service.BookService;

@RestController
@RequestMapping("/api/book")
public class PageController {

    @Autowired
    private BookService bookService;

    @GetMapping("/getBooksByTitle")
    public ResponseEntity<List<BookDto>> getBooksByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int pageNo) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<BookDto> books = bookService.getBooksByTitle(title, pageable); // Update to use BookDto

        return books.hasContent()
            ? ResponseEntity.ok(books.getContent()) // Return List<BookDto>
            : ResponseEntity.noContent().build(); // 204 No Content
    }
}

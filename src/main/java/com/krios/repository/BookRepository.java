package com.krios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.krios.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
    // Method to find books with titles containing the specified string
    Page<Book> findByTitleContaining(String title, Pageable pageable);
}

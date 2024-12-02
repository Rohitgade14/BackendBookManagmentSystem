package com.krios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.krios.entity.Book;

@Repository
public interface PdfRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.users WHERE b.id = :id")
    @EntityGraph(attributePaths = {"users"}) // Eagerly fetch associated users
    Optional<Book> findByIdWithUsers(@Param("id") Integer id); 
}

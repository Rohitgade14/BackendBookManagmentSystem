package com.krios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.krios.entity.Book;

@Repository
public interface ExcelRepository extends JpaRepository<Book, Integer> {
   
}

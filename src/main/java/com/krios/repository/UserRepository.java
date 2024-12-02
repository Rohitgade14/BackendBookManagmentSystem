package com.krios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.krios.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Method to find all users associated with a particular book by the book's ID
    List<User> findByBookId(Integer bookId);

	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	
}

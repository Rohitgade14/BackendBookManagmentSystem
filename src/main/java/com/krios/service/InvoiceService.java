package com.krios.service;

import com.krios.dto.BookDto;
import com.krios.dto.InvoiceDetails;
import com.krios.dto.UserDto;
import com.krios.entity.Book;
import com.krios.entity.User;
import com.krios.exception.BookNotFoundException;
import com.krios.repository.BookRepository;
import com.krios.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    public InvoiceDetails getBookWithUsers(Integer bookId) {
        logger.info("Fetching book with ID: {}", bookId);
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", bookId);
                    return new BookNotFoundException("Book not found with id: " + bookId);
                });

        List<User> users = userRepository.findByBookId(bookId);
        logger.info("Fetched book: {} with {} associated users", book.getTitle(), users.size());

        // Convert Book and User entities to their respective DTOs
        BookDto bookDto = new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), 
                                       users.stream().map(this::convertToUserDto).collect(Collectors.toList()));

        return new InvoiceDetails(bookDto, users.stream().map(this::convertToUserDto).collect(Collectors.toList()));
    }

    // Convert User entity to UserDto
    private UserDto convertToUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getLocation()); 
    }
}

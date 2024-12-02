package com.krios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krios.dto.BookDto;
import com.krios.dto.UserDto;
import com.krios.entity.Book;
import com.krios.entity.User;
import com.krios.repository.BookRepository;
import com.krios.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Transactional
    public List<BookDto> createBooks(List<BookDto> bookDtos) {
        logger.info("Creating books: {}", bookDtos);
        List<BookDto> savedBookDtos = new ArrayList<>();

        for (BookDto bookDto : bookDtos) {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(bookDto.getAuthor());
            book.setPrice(bookDto.getPrice());

            Book savedBook = bookRepository.save(book);
            logger.info("Saved book with ID: {}", savedBook.getId());

            BookDto savedBookDto = new BookDto();
            savedBookDto.setId(savedBook.getId());
            savedBookDto.setTitle(savedBook.getTitle());
            savedBookDto.setAuthor(savedBook.getAuthor());
            savedBookDto.setPrice(savedBook.getPrice());

            if (bookDto.getUsers() != null) {
                List<UserDto> savedUserDtos = new ArrayList<>();
                for (UserDto userDto : bookDto.getUsers()) {
                    User user = new User();
                    user.setName(userDto.getName());
                    user.setEmail(userDto.getEmail());
                    user.setLocation(userDto.getLocation());
                    user.setBook(savedBook);

                    try {
                        User savedUser = userRepository.save(user);
                        logger.info("Saved user: {} with ID: {}", savedUser.getName(), savedUser.getId());

                        UserDto savedUserDto = new UserDto();
                        savedUserDto.setName(savedUser.getName());
                        savedUserDto.setEmail(savedUser.getEmail());
                        savedUserDto.setLocation(savedUser.getLocation());
                        savedUserDtos.add(savedUserDto);
                    } catch (ConstraintViolationException e) {
                        logger.error("Failed to save user: {}. Error: {}", user.getEmail(), e.getMessage());
                    }
                }
                savedBookDto.setUsers(savedUserDtos);
            }
            savedBookDtos.add(savedBookDto);
        }
        return savedBookDtos;
    }

    @Override
    public List<BookDto> getAllBooks() {
        logger.info("Fetching all books");
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> getBookById(Integer id) {
        logger.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        logger.info("Creating book: {}", bookDto);
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());

        Book savedBook = bookRepository.save(book);
        logger.info("Saved book with ID: {}", savedBook.getId());
        return convertToDto(savedBook);
    }

    @Override
    @Transactional
    public Optional<BookDto> updateBook(Integer id, BookDto bookDetails) {
        logger.info("Updating book with ID: {}", id);
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPrice(bookDetails.getPrice());

            Book updatedBook = bookRepository.save(book);
            logger.info("Updated book with ID: {}", updatedBook.getId());
            return convertToDto(updatedBook);
        });
    }

    @Override
    @Transactional
    public BookDto deleteBook(Integer id) {
        logger.info("Deleting book with ID: {}", id);
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            bookRepository.delete(bookOptional.get());
            logger.info("Deleted book with ID: {}", id);
            return convertToDto(bookOptional.get());
        }
        logger.warn("Book with ID: {} not found for deletion", id);
        return null;
    }

    @Override
    public Page<BookDto> getBooksByTitle(String title, Pageable pageable) {
        logger.info("Fetching books with title containing: {}", title);
        Page<Book> bookPage = bookRepository.findByTitleContaining(title, pageable);
        List<BookDto> bookDtos = bookPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(bookDtos, pageable, bookPage.getTotalElements());
    }

    private BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPrice(book.getPrice());

        List<UserDto> userDtos = book.getUsers().stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setName(user.getName());
                    userDto.setEmail(user.getEmail());
                    userDto.setLocation(user.getLocation());
                    return userDto;
                })
                .collect(Collectors.toList());

        bookDto.setUsers(userDtos);
        return bookDto;
    }
}

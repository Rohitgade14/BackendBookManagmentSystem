package com.krios.service.excelService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.krios.entity.Book;
import com.krios.entity.User;
import com.krios.repository.BookRepository;
import com.krios.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExcelExportService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);

    public void importBooksAndUsers(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<Integer, Book> bookMap = new HashMap<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Read and validate the current book
                Book currentBook = readBookData(row);
                if (currentBook != null) {
                    // Check if the book already exists by ID
                    Book existingBook = bookRepository.findById(currentBook.getId()).orElse(null);
                    if (existingBook == null) {
                        // Save new book
                        bookRepository.save(currentBook);
                        logger.info("Saved new book: {}", currentBook);
                    } else {
                        // Update existing book details
                        logger.info("Book with ID {} found, updating book information.", existingBook.getId());
                        updateBook(existingBook, currentBook);
                        bookRepository.save(existingBook);
                        logger.info("Updated book: {}", existingBook);
                    }

                    // Read user data
                    User user = readUserData(row);
                    if (user != null) {
                        user.setBook(currentBook); // Associate user with the book

                        // Check for existing user by ID
                        User existingUser = userRepository.findById(user.getId()).orElse(null);
                        if (existingUser == null) {
                            // If user doesn't exist, check by email
                            existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
                        }

                        // Decide to save a new user or update an existing one
                        if (existingUser == null) {
                            userRepository.save(user); // Save new user
                            logger.info("Saved new user: {}", user);
                        } else {
                            // Update existing user details
                            logger.info("User with ID {} found, updating user information.", existingUser.getId());
                            updateUser(existingUser, user, currentBook); // Update details
                            userRepository.save(existingUser); // Save updated user
                            logger.info("Updated user: {}", existingUser);
                        }
                    }
                }
            }

            logger.info("Data imported successfully from Excel");
        } catch (IOException e) {
            logger.error("Error occurred while reading the Excel file: {}", e.getMessage());
            throw new RuntimeException("Error reading Excel file", e);
        }
    }

    private void updateUser(User existingUser, User newUser, Book currentBook) {
        existingUser.setName(newUser.getName()); // Update name
        existingUser.setEmail(newUser.getEmail()); // Update email if needed
        existingUser.setLocation(newUser.getLocation()); // Update location
        existingUser.setBook(currentBook); // Update associated book
    }

    private void updateBook(Book existingBook, Book newBook) {
        existingBook.setTitle(newBook.getTitle()); // Update title
        existingBook.setAuthor(newBook.getAuthor()); // Update author
        existingBook.setPrice(newBook.getPrice()); // Update price
    }

    private Book readBookData(Row row) {
        Book book = new Book();
        Integer bookId = getCellValueAsInteger(row.getCell(0)); // Book ID
        if (bookId != null) {
            book.setId(bookId); // Set Book ID

            String bookTitle = getCellValue(row.getCell(1)); // Title
            String author = getCellValue(row.getCell(2)); // Author

            if (bookTitle != null && !bookTitle.isEmpty()) {
                book.setTitle(bookTitle);
                book.setAuthor(author);

                // Parse price
                String priceStr = getCellValue(row.getCell(3)); // Price
                try {
                    book.setPrice(Double.parseDouble(priceStr));
                } catch (NumberFormatException e) {
                    logger.error("Invalid price format in row {}: {}", row.getRowNum(), priceStr);
                    return null;
                }
            } else {
                logger.warn("Book title is missing in row {}: skipping row.", row.getRowNum());
                return null;
            }
        } else {
            logger.warn("Book ID is missing in row {}: skipping row.", row.getRowNum());
            return null;
        }

        return book;
    }

    private User readUserData(Row row) {
        User user = new User();
        Integer userId = getCellValueAsInteger(row.getCell(4)); // User ID
        if (userId != null) {
            user.setId(userId); // Set User ID

            String email = getCellValue(row.getCell(5)); // Email
            if (!isEmailValid(email)) {
                logger.warn("Invalid email format in row {}: {}", row.getRowNum(), email);
                return null;
            }
            user.setEmail(email);
            user.setLocation(getCellValue(row.getCell(6))); // Location
            user.setName(getCellValue(row.getCell(7))); // User Name

            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getLocation().isEmpty()) {
                logger.warn("Incomplete user data in row {}: {}", row.getRowNum(), user);
                return null;
            }
        } else {
            logger.warn("User ID is missing in row {}: skipping row.", row.getRowNum());
            return null;
        }

        return user;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    logger.warn("Invalid integer format in cell: {}", cell.getStringCellValue());
                    return null;
                }
            default:
                return null;
        }
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }
}

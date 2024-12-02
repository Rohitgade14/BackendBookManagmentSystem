package com.krios.service.excelService;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krios.entity.Book;
import com.krios.entity.User;
import com.krios.repository.ExcelRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelRepository excelRepository;

    private final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Override
    public void exportBooksAndUsersToExcel(HttpServletResponse response) {
        logger.info("Starting export of books and users to Excel");

        // Set response properties
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=books_and_users.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Books and Users");
            createBooksAndUsersHeaderRow(sheet);

            // Fetch all books from the repository
            List<Book> books = excelRepository.findAll();
            logger.info("Fetched {} books from the database", books.size());

            int rowCount = 1; // Start from the second row
            for (Book book : books) {
                List<User> users = book.getUsers(); // Assuming Book has a method to get associated users

                // Write book data for each associated user
                if (users != null && !users.isEmpty()) {
                    for (User user : users) {
                        Row row = sheet.createRow(rowCount++); // Create a new row for each user
                        writeBookAndUserData(book, user, row); // Write both book and user data
                    }
                } else {
                    // If no users associated, still write the book info
                    Row row = sheet.createRow(rowCount++);
                    writeBookDataOnly(book, row); // Write only book data with empty user fields
                }
            }

            // Write the workbook to the output stream
            workbook.write(response.getOutputStream());
            logger.info("Successfully exported books and users to Excel");
        } catch (IOException e) {
            logger.error("Error occurred while exporting books and users to Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Error exporting books and users to Excel", e);
        }
    }

    // Method to create header row with increased size and adjusted column widths
    private void createBooksAndUsersHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);

        // Create a CellStyle for the header row (bold and larger font size)
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true); // Make font bold
        font.setFontHeightInPoints((short) 16); // Increase font size to 16
        headerStyle.setFont(font);

        // Set header values with the new style
        String[] headers = {"Book ID", "Title", "Author", "Price", "User ID", "User Email", "User Location", "User Name"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle); // Apply the style to each header cell

            // Set fixed column width for better readability
            switch (i) {
                case 0: // Book ID
                    sheet.setColumnWidth(i, 10 * 256);
                    break;
                case 1: // Title
                    sheet.setColumnWidth(i, 35 * 256);
                    break;
                case 2: // Author
                    sheet.setColumnWidth(i, 20 * 256);
                    break;
                case 3: // Price
                    sheet.setColumnWidth(i, 10 * 256);
                    break;
                case 4: // User ID
                    sheet.setColumnWidth(i, 10 * 256);
                    break;
                case 5: // User Email
                    sheet.setColumnWidth(i, 40 * 256);
                    break;
                case 6: // User Location
                    sheet.setColumnWidth(i, 20 * 256);
                    break;
                case 7: // User Name
                    sheet.setColumnWidth(i, 20 * 256);
                    break;
                default:
                    break;
            }
        }
    }

    private void writeBookAndUserData(Book book, User user, Row row) {
        // Write book data
        row.createCell(0).setCellValue(book.getId()); // Book ID
        row.createCell(1).setCellValue(book.getTitle()); // Title
        row.createCell(2).setCellValue(book.getAuthor()); // Author
        row.createCell(3).setCellValue(book.getPrice()); // Price

        // Write user data
        row.createCell(4).setCellValue(user.getId()); // User ID
        row.createCell(5).setCellValue(user.getEmail()); // User Email
        row.createCell(6).setCellValue(user.getLocation()); // User Location
        row.createCell(7).setCellValue(user.getName()); // User Name
    }

    private void writeBookDataOnly(Book book, Row row) {
        // Write only book data with empty user fields
        row.createCell(0).setCellValue(book.getId()); // Book ID
        row.createCell(1).setCellValue(book.getTitle()); // Title
        row.createCell(2).setCellValue(book.getAuthor()); // Author
        row.createCell(3).setCellValue(book.getPrice()); // Price

        // Leave user fields empty
        row.createCell(4).setCellValue(""); // User ID
        row.createCell(5).setCellValue(""); // User Email
        row.createCell(6).setCellValue(""); // User Location
        row.createCell(7).setCellValue(""); // User Name
    }
}

package com.krios.service.pdfService;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.krios.entity.Book;
import com.krios.exception.PdfGenerationException;
import com.krios.repository.PdfRepository;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private PdfRepository pdfRepository;

    private final Logger logger = LoggerFactory.getLogger(PdfServiceImpl.class);

    @Override
    public Optional<Book> exportBookToPdf(Integer id, HttpServletResponse response) {
        logger.info("Attempting to export book with ID: {}", id);
        Optional<Book> bookOptional = pdfRepository.findByIdWithUsers(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            logger.info("Found book: {}", book.getTitle());

            // Debugging: Print the number of users fetched
            logger.debug("Number of users associated with the book: {}", book.getUsers().size());

            Document document = new Document();

            try {
                // Set the content type and header for the response
                response.setContentType("application/pdf");
                String fileName = "book-receipt-" + book.getTitle().replaceAll("\\s+", "_") + ".pdf";
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                // Create the PDF document
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                // Add content to the PDF using helper methods
                PdfDocumentHelper.addBookReceiptHeader(document, book);
                PdfDocumentHelper.addBookDetailsTable(document, book);
                PdfDocumentHelper.addThankYouNote(document);
                PdfDocumentHelper.addFooter(document);

                logger.info("PDF document created successfully for book: {}", book.getTitle());
            } catch (DocumentException | IOException e) {
                logger.error("Error while exporting book to PDF: {}", e.getMessage(), e);
                throw new PdfGenerationException("Error while exporting book to PDF", e); // Custom exception
            } finally {
                if (document.isOpen()) {
                    document.close();
                }
            }
            return bookOptional; // Return the book if PDF was generated
        }

        logger.warn("Book with ID: {} not found", id);
        return Optional.empty(); // Return empty if book not found
    }
}

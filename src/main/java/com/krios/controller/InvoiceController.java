package com.krios.controller;

import com.krios.dto.InvoiceDetails;
import com.krios.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    // Constructor-based injection
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Endpoint to get a book and its associated users
    @GetMapping("/{bookId}/users")
    public ResponseEntity<InvoiceDetails> getBookWithUsers(@PathVariable Integer bookId) {
        logger.info("Fetching book with ID: {}", bookId); // Log request

        try {
            InvoiceDetails invoiceDetails = invoiceService.getBookWithUsers(bookId);
            if (invoiceDetails == null) {
                logger.warn("Book with ID: {} not found", bookId); // Log warning
                return ResponseEntity.notFound().build(); // Return 404 if no book found
            }
            logger.info("Successfully fetched book with ID: {}", bookId); // Log success
            return ResponseEntity.ok(invoiceDetails);

        } catch (Exception e) {
            logger.error("Error fetching book with ID: {}", bookId, e); // Log error details
            return ResponseEntity.status(500).body(null); // Return 500 for unexpected errors
        }
    }
}

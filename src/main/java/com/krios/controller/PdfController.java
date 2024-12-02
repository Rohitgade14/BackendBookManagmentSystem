
package com.krios.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krios.entity.Book;
import com.krios.service.pdfService.PdfService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @GetMapping("/{id}")
    public void exportBookToPdf(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        // Set headers for PDF file
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book_" + id + ".pdf");

        // Generate the PDF and write it to the response's output stream
        Optional<Book> bookOptional = pdfService.exportBookToPdf(id, response);

        if (!bookOptional.isPresent()) {
            // If the book is not found, set response status to 404
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().write("Book not found");
            response.getWriter().flush();
        }
    }
}









//package com.krios.controller;
//
//import java.io.IOException;
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.krios.entity.Book;
//import com.krios.service.pdfService.PdfService;
//import jakarta.servlet.http.HttpServletResponse;
//
//@RestController
//@RequestMapping("/api/pdf")
//public class PdfController {
//    @Autowired
//    private PdfService pdfService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Void> exportBookToPdf(@PathVariable Integer id, HttpServletResponse response) throws IOException {
//        Optional<Book> bookOptional = pdfService.exportBookToPdf(id, response);
//
//        if (bookOptional.isPresent()) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}

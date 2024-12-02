package com.krios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krios.service.excelService.ExcelService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/excel")
public class ExcelImportController {

    @Autowired
    private ExcelService excelService; 

    @GetMapping("/download")
    public ResponseEntity<String> exportBooksAndUsersToExcel(HttpServletResponse response) {
        try {
            excelService.exportBooksAndUsersToExcel(response); // Call service to export books and users to Excel
            return ResponseEntity.ok("Books and users exported successfully."); // Return success message
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to export books and users: " + e.getMessage());
        }
    }
}

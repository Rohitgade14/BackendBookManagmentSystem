package com.krios.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.krios.service.excelService.ExcelExportService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelExportController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportController.class);

    @Autowired
    private ExcelExportService excelExportService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("File upload initiated for file: {}", file.getOriginalFilename());

        // Validate file type (only .xls and .xlsx files)
        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            logger.error("Invalid file type: {}", fileName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Please upload a valid Excel file.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // Process the uploaded file
            excelExportService.importBooksAndUsers(file);
            logger.info("File upload completed successfully for file: {}", file);

            // Success response
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded and data stored successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log and return error if file processing fails
            logger.error("Failed to store data: {}", e.getMessage(), e);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to store data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

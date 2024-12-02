package com.krios.service.pdfService;

import java.util.Optional;
import com.krios.entity.Book;
import jakarta.servlet.http.HttpServletResponse;

public interface PdfService {
    Optional<Book> exportBookToPdf(Integer id, HttpServletResponse response);
}

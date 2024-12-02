package com.krios.service.excelService;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {
	void exportBooksAndUsersToExcel(HttpServletResponse response);
}

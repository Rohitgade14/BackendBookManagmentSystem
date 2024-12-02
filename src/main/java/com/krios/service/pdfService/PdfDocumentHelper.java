package com.krios.service.pdfService;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.krios.entity.Book;
import com.krios.entity.User;

import java.util.List;

public class PdfDocumentHelper {

    public static void addBookReceiptHeader(Document document, Book book) throws DocumentException {
        addLogoToDocument(document); // Add logo to the document

        document.add(new Paragraph("Book Receipt", PdfFontHelper.getTitleFont()));
        document.add(new Paragraph(" ")); // Add space
    }

    private static void addLogoToDocument(Document document) throws DocumentException {
        try {
            String logoPath = "src/main/resources/static/Images/QrLogo.png"; // Path to logo
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(140, 120); // Scale image
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo); // Add logo to document
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions if logo cannot be loaded
        }
    }

    public static void addBookDetailsTable(Document document, Book book) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        addTableHeader(table); // Add headers

        // Add book details
        addBookDetailRow(table, "Book ID", String.valueOf(book.getId()));
        addBookDetailRow(table, "Title", book.getTitle());
        addBookDetailRow(table, "Author", book.getAuthor());
        addBookDetailRow(table, "Price", String.format("$%.2f", book.getPrice()));

        // Add users
        addUsersToTable(table, book.getUsers());

        document.add(table); // Add the table to the document
        document.add(new Paragraph(" ")); // Add space
    }

    private static void addTableHeader(PdfPTable table) {
        table.addCell(createHeaderCell("Field"));
        table.addCell(createHeaderCell("Value"));
    }

    private static PdfPCell createHeaderCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content, PdfFontHelper.getBoldFont(12)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        return cell;
    }

    private static void addBookDetailRow(PdfPTable table, String field, String value) {
        table.addCell(field);
        table.addCell(value);
    }

    private static void addUsersToTable(PdfPTable table, List<User> users) {
        table.addCell("Users");
        
        if (users != null && !users.isEmpty()) {
            StringBuilder userDetails = new StringBuilder(); // Use StringBuilder for efficiency
            for (User user : users) {
                String userDetail = String.format("%s (ID: %d, Email: %s, Location: %s)", 
                    user.getName(), user.getId(), user.getEmail(), user.getLocation());
                userDetails.append(userDetail).append("\n"); // Append each user detail followed by a new line
            }
            table.addCell(userDetails.toString()); // Add all user details as a single cell entry
        } else {
            table.addCell("No users");
        }
    }


    public static void addThankYouNote(Document document) throws DocumentException {
        document.add(new Paragraph("Thank you for your purchase!", PdfFontHelper.getNormalFont()));
    }

    public static void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph("This is a computer-generated document. No signature required.", PdfFontHelper.getNormalFont()));
    }
}

package com.krios.service.pdfService;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.BaseColor;

public class PdfFontHelper {

    // Default Font Settings
    private static final String DEFAULT_BASE_FONT = BaseFont.HELVETICA;

    // Font Sizes
    private static final float TITLE_FONT_SIZE = 32f;
    private static final float NORMAL_FONT_SIZE = 28f;
    private static final float BOLD_FONT_SIZE = 32f; 
    private static final float ITALIC_FONT_SIZE = 32f;

    public static Font getTitleFont() {
        return getFont(DEFAULT_BASE_FONT, TITLE_FONT_SIZE, Font.UNDERLINE, BaseColor.PINK);
    }

    public static Font getNormalFont() {
        return getFont(DEFAULT_BASE_FONT, NORMAL_FONT_SIZE, Font.NORMAL, BaseColor.RED);
    }

    public static Font getBoldFont() {
        return getFont(DEFAULT_BASE_FONT, BOLD_FONT_SIZE, Font.BOLD, BaseColor.ORANGE);
    }

    public static Font getItalicFont() {
        return getFont(DEFAULT_BASE_FONT, ITALIC_FONT_SIZE, Font.ITALIC, BaseColor.BLUE);
    }

    public static Font getUnderlinedFont(float size) {
        return getFont(DEFAULT_BASE_FONT, size, Font.UNDERLINE, BaseColor.GREEN);
    }

    public static Font getColoredFont(float size, BaseColor color) {
        return getFont(DEFAULT_BASE_FONT, size, Font.NORMAL, color);
    }

    public static Font getCustomFont(String baseFont, float size, int style, BaseColor color) {
        return FontFactory.getFont(baseFont, size, style, color);
    }

    private static Font getFont(String baseFont, float size, int style, BaseColor color) {
        return FontFactory.getFont(baseFont, size, style, color);
    }

    public static Font getBoldFont(float size) {
        return getFont(DEFAULT_BASE_FONT, size, Font.BOLD, BaseColor.PINK);
    }
}

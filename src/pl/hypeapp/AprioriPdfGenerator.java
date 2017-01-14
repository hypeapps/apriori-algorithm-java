package pl.hypeapp;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class AprioriPdfGenerator {
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private List<Pair<String, Float>> strongRules;
    private Document document;
    private File file;
    private Pair<Float, Float> supportConfidencePair;

    public AprioriPdfGenerator(List<Pair<String, Float>> strongRules, Pair<Float, Float> supportConfidencePair) {
        this.strongRules = strongRules;
        this.document = new Document();
        this.supportConfidencePair = supportConfidencePair;
    }

    public Document generateDocument(FileOutputStream fileOutputStream) throws DocumentException {
        pdfWriter(fileOutputStream);
        this.document.open();
        addMetaData(this.document);
        addHeader(this.document);
        createTable(this.document);
        this.document.close();
        return this.document;
    }

    private void addMetaData(Document document) {
        document.addTitle("Apriori algorithm");
        document.addSubject("Apriori strong rules");
        document.addAuthor("Przemysław Szymkowiak");
        document.addCreator("Przemysław Szymkowiak");
    }

    public void pdfWriter(FileOutputStream fileOutputStream) throws DocumentException {
        PdfWriter.getInstance(this.document, fileOutputStream);
    }

    private void addHeader(Document document) throws DocumentException {
        Paragraph header = new Paragraph();
        addEmptyLine(header, 1);
        header.add(new Paragraph("Apriori algorithm", headerFont));
        addEmptyLine(header, 1);
        float support = supportConfidencePair.getLeft() * 100f;
        header.add(new Paragraph("Minimum support " + support + "%", smallBold));
        float confidence = supportConfidencePair.getRight() * 100f;
        header.add(new Paragraph("Minimum confidence " + confidence + "%", smallBold));
        addEmptyLine(header, 3);
        document.add(header);
    }

    private void createTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell pdfCell = new PdfPCell(new Phrase("Rules"));
        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfCell);
        pdfCell = new PdfPCell(new Phrase("Confidence"));
        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfCell);
        strongRules.forEach((rule) -> {
            table.addCell(rule.getLeft());
            float confidence = rule.getRight() * 100f;
            table.addCell(String.format("%.2f", confidence) + "%");
        });
        document.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}

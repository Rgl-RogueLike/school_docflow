package com.haritonov.school.docflow.modules.printdocuments.service;
import com.haritonov.school.docflow.modules.certificate.dto.CertificateResponse;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationResponse;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreItemResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentTemplateService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Печать приказа о зачислении
     */
    public byte[] generateEnrollmentOrder(EnrollmentResponse enrollment) throws Exception {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{documentDate}", enrollment.getDocumentDate().format(DATE_FORMATTER));
        placeholders.put("{DocumentNumber}", enrollment.getDocumentNumber());
        placeholders.put("{className}", enrollment.getClassName());
        placeholders.put("{enrollmentDate}", enrollment.getEnrollmentDate().format(DATE_FORMATTER));
        placeholders.put("{studentFullName}", enrollment.getStudentFullName());
        placeholders.put("{studentDateOfBirth}", enrollment.getDateOfBirth() != null ? enrollment.getDateOfBirth().format(DATE_FORMATTER) : "__________");
        placeholders.put("{employee}", enrollment.getCreatorFullName() != null ? enrollment.getCreatorFullName() : "___________________");
        placeholders.put("{director}", "___________________"); // заглушка

        return processTemplate("templates/doc-templates/enrollment.docx", placeholders);
    }


    /**
     * Печать приказа о зачислении (групповой)
     */
    public byte[] generateEnrollmentMoreOrder(EnrollmentMoreResponse enrollment) throws Exception {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{documentDate}", enrollment.getDocumentDate().format(DATE_FORMATTER));
        placeholders.put("{documentNumber}", enrollment.getDocumentNumber());
        placeholders.put("{employee}", enrollment.getCreatorFullName());
        placeholders.put("{className}", enrollment.getClassName());
        placeholders.put("{director}", "___________________");

        return processTemplateWithTable("templates/doc-templates/enrollment-more.docx", placeholders, enrollment.getItems());
    }

    private byte[] processTemplateWithTable(String templatePath, Map<String, String> placeholders,
                                            List<EnrollmentMoreItemResponse> items) throws Exception {
        InputStream templateStream = new ClassPathResource(templatePath).getInputStream();
        XWPFDocument doc = new XWPFDocument(templateStream);

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            replaceInParagraph(paragraph, placeholders);
        }

        List<XWPFTable> tables = doc.getTables();
        if (!tables.isEmpty()) {
            XWPFTable table = tables.get(0);
            while (table.getNumberOfRows() > 1) {
                table.removeRow(1);
            }
            int rowIndex = 1;
            for (EnrollmentMoreItemResponse item : items) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(rowIndex));
                row.getCell(1).setText(item.getStudentFullName());
                row.getCell(2).setText(item.getDateOfBirth().format(DATE_FORMATTER) + " г.");
                rowIndex++;
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Печать приказа об освобождении
     */
    public byte[] generateExemptionOrder(ExemptionOrderResponse exemption) throws Exception {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{documentDate}", exemption.getDocumentDate().format(DATE_FORMATTER));
        placeholders.put("{documentNumber}", exemption.getDocumentNumber());
        placeholders.put("{basis}", exemption.getPurpose() != null ? exemption.getPurpose() : "");
        placeholders.put("{effectiveStartDate}", exemption.getDateFrom().format(DATE_FORMATTER));
        placeholders.put("{effectiveEndDate}", exemption.getIssueDate().format(DATE_FORMATTER));
        placeholders.put("{studentFullName}", exemption.getStudentFullName());
        placeholders.put("{studentDateOfBirth}", exemption.getStudentDateOfBirth() != null ? exemption.getStudentDateOfBirth().format(DATE_FORMATTER) : "");
        placeholders.put("{className}", exemption.getStudentClassName() != null ? exemption.getStudentClassName() : "");
        placeholders.put("{director}", "___________________");

        return processTemplate("templates/doc-templates/exemption-order.docx", placeholders);
    }

    public byte[] generateCertificate(CertificateResponse certificate) throws Exception {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{documentNumber}", certificate.getDocumentNumber());
        placeholders.put("{dateOfDocument}", certificate.getDocumentDate().format(DATE_FORMATTER));
        placeholders.put("{studentFullName}", certificate.getStudentFullName());
        placeholders.put("{studentDateOfBirth}", certificate.getStudentDateOfBirth().format(DATE_FORMATTER));
        placeholders.put("{nowDate}", certificate.getAcademicYear()); // или вычислить текущий учебный год
        placeholders.put("{className}", certificate.getStudentClassName());
        placeholders.put("{Director}", "___________________"); // заглушка
        return processTemplate("templates/doc-templates/certificate.docx", placeholders);
    }

    public byte[] generateDislocationOrder(DislocationResponse dislocation) throws Exception {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{documentDate}", dislocation.getDocumentDate().format(DATE_FORMATTER));
        placeholders.put("{documentNumber}", dislocation.getDocumentNumber());
        placeholders.put("{basis}", dislocation.getBasis() != null ? dislocation.getBasis() : "");
        placeholders.put("{effectiveStartDate}", dislocation.getEffectiveStartDate().format(DATE_FORMATTER));
        placeholders.put("{effectiveEndDate}", dislocation.getEffectiveEndDate().format(DATE_FORMATTER));
        placeholders.put("{studentFullName}", dislocation.getStudentFullName());
        placeholders.put("{studentDateOfBirth}", dislocation.getStudentDateOfBirth() != null ? dislocation.getStudentDateOfBirth().format(DATE_FORMATTER) : "");
        placeholders.put("{className}", dislocation.getStudentClassName() != null ? dislocation.getStudentClassName() : "");
        placeholders.put("{director}", "___________________");
        return processTemplate("templates/doc-templates/dislocation.docx", placeholders);
    }

    private byte[] processTemplate(String templatePath, Map<String, String> placeholders) throws Exception {
        InputStream templateStream = new ClassPathResource(templatePath).getInputStream();
        XWPFDocument doc = new XWPFDocument(templateStream);

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            replaceInParagraph(paragraph, placeholders);
        }

        doc.getTables().forEach(table ->
                table.getRows().forEach(row ->
                        row.getTableCells().forEach(cell ->
                                cell.getParagraphs().forEach(para -> replaceInParagraph(para, placeholders))
                        )
                )
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        doc.close();
        return baos.toByteArray();
    }

    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        String text = paragraph.getText();
        if (text == null) return;

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (text.contains(key)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String runText = run.getText(0);
                    if (runText != null && runText.contains(key)) {
                        run.setText(runText.replace(key, value), 0);
                    }
                }
            }
        }
    }
}

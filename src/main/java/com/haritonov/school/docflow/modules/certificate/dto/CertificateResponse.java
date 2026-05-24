package com.haritonov.school.docflow.modules.certificate.dto;

import com.haritonov.school.docflow.modules.certificate.model.enums.CertificateStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CertificateResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String purpose;
    private LocalDate dateFrom;
    private LocalDate issueDate;
    private String studentFullName;
    private Long studentId;
    private String creatorFullName;
    private CertificateStatus status;
}

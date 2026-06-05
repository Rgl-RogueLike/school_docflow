package com.haritonov.school.docflow.modules.certificate.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CertificateResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String academicYear;
    private Long studentId;
    private String studentFullName;
    private LocalDate studentDateOfBirth;
    private String studentClassName;
    private String creatorFullName;
}
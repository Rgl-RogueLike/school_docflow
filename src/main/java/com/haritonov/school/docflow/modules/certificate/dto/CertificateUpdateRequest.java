package com.haritonov.school.docflow.modules.certificate.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CertificateUpdateRequest {
    private Long id;
    private String documentNumber;
    private LocalDate documentDate;
    private String academicYear;
    private Long studentId;
}
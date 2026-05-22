package com.haritonov.school.docflow.modules.release.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReleaseResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String basis;
    private LocalDate basisDocumentDate;
    private LocalDate dateFrom;
    private LocalDate dateOn;
    private Long studentId;
    private String studentFullName;
    private String studentClassName;
    private String creatorFullName;
}
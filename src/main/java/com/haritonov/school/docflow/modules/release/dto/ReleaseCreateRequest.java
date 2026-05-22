package com.haritonov.school.docflow.modules.release.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReleaseCreateRequest {
    private String documentNumber;
    private LocalDate documentDate;
    private String basis;
    private LocalDate basisDocumentDate;
    private LocalDate dateFrom;
    private LocalDate dateOn;
    private Long studentId;
    private Long creatorId;
}
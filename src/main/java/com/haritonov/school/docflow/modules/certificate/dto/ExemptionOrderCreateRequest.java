package com.haritonov.school.docflow.modules.certificate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExemptionOrderCreateRequest {

    private String documentNumber;
    private LocalDate documentDate;
    private String purpose;
    private LocalDate dateFrom;
    private LocalDate issueDate;
    private Long studentId;
    private Long creatorId;
}

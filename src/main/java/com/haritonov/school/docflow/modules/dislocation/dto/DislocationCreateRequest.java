package com.haritonov.school.docflow.modules.dislocation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DislocationCreateRequest {
    private String documentNumber;
    private LocalDate documentDate;
    private String relation;
    private String basis;
    private LocalDate dateFrom;
    private LocalDate effectiveStartDate;
    private LocalDate effectiveEndDate;
    private Long studentId;
    private Long creatorId;
}

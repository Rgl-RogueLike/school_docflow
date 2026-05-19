package com.haritonov.school.docflow.modules.dislocation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DislocationResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String relation;
    private String basis;
    private LocalDate dateFrom;
    private LocalDate effectiveStartDate;
    private LocalDate effectiveEndDate;
    private String studentFullName;
    private String creatorFullName;
}

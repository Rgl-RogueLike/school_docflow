package com.haritonov.school.docflow.modules.dislocation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class DislocationFilterDto {

    private String documentNumber;

    private String studentName;

    private String creatorName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveStartDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveStartDateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveEndDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveEndDateTo;
}
package com.haritonov.school.docflow.modules.enrollment.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class EnrollmentFilterDto {

    private String documentNumber;

    private String studentName;

    private String creatorName;

    private String className;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDateTo;
}
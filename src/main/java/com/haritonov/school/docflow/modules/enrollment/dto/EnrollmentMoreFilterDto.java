package com.haritonov.school.docflow.modules.enrollment.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class EnrollmentMoreFilterDto {

    private String documentNumber;

    private String creatorName;

    private String className;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDateTo;
}
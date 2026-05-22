package com.haritonov.school.docflow.modules.release.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class ReleaseFilterDto {

    private String documentNumber;

    private String studentName;

    private String creatorName;

    private String className;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate basisDocumentDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate basisDocumentDateTo;
}
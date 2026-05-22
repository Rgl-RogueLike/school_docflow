package com.haritonov.school.docflow.modules.enrollment.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class EnrollmentMoreCreateRequest {
    private String documentNumber;
    private LocalDate documentDate;
    private String basis;
    private LocalDate dateFrom;
    private Long classId;
    private Long creatorId;

    private Integer rowCount;

    private List<EnrollmentMoreItemDto> items;
}
package com.haritonov.school.docflow.modules.enrollment.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnrollmentMoreResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String basis;
    private LocalDate dateFrom;
    private String className;
    private Long classId;
    private String creatorFullName;
    private List<EnrollmentMoreItemResponse> items;
}
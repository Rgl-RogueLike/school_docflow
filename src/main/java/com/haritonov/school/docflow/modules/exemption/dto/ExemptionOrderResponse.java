package com.haritonov.school.docflow.modules.exemption.dto;

import com.haritonov.school.docflow.modules.exemption.model.enums.ExemptionOrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExemptionOrderResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String purpose;
    private LocalDate dateFrom;
    private LocalDate issueDate;
    private String studentFullName;
    private Long studentId;
    private String creatorFullName;
    private ExemptionOrderStatus status;
    private LocalDate studentDateOfBirth;
    private String studentClassName;
}

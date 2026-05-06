package com.haritonov.school.docflow.modules.student.dto;

import lombok.Data;

@Data
public class EducationalClassCreateRequest {
    private String name;
    private Integer year;
}

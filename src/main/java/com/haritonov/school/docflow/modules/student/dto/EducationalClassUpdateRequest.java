package com.haritonov.school.docflow.modules.student.dto;

import lombok.Data;

@Data
public class EducationalClassUpdateRequest {
    private Long id;
    private String name;
    private Integer year;
}

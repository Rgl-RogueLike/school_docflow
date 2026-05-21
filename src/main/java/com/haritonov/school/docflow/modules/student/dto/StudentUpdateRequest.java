package com.haritonov.school.docflow.modules.student.dto;

import com.haritonov.school.docflow.modules.student.model.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentUpdateRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Gender gender;
    private LocalDate dateOfBirth;
    private LocalDate dateOfEnrollment;
    private Long classId;
}

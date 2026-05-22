package com.haritonov.school.docflow.modules.enrollment.dto;

import com.haritonov.school.docflow.modules.student.model.enums.Gender;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EnrollmentMoreItemResponse {
    private Long id;
    private Long studentId;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Gender gender;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private String studentFullName;
}
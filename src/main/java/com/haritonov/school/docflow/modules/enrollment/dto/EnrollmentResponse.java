package com.haritonov.school.docflow.modules.enrollment.dto;

import com.haritonov.school.docflow.modules.student.model.enums.Gender;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EnrollmentResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime documentDate;
    private LocalDate enrollmentDate;
    private String reason;

    private Long studentId;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String studentFullName;

    private String className;
    private Long classId;
    private String creatorFullName;
}
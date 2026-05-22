package com.haritonov.school.docflow.modules.enrollment.dto;

import com.haritonov.school.docflow.modules.student.model.enums.Gender;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EnrollmentCreateRequest {
    private String documentNumber;
    private LocalDate documentDate;
    private LocalDate enrollmentDate;
    private String reason;

    private String lastName;
    private String firstName;
    private String patronymic;
    private Gender gender;
    private LocalDate dateOfBirth;

    private Long classId;
    private Long creatorId;
}
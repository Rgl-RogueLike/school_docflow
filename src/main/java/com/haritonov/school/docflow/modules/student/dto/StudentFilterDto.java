package com.haritonov.school.docflow.modules.student.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class StudentFilterDto {

    private String lastName;

    private String firstName;

    private String patronymic;

    private String className;

    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirthFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirthTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfEnrollmentFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfEnrollmentTo;
}
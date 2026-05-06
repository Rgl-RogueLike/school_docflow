package com.haritonov.school.docflow.modules.student.dto;

import com.haritonov.school.docflow.modules.student.model.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String className;

    public String getFullName() {
        return lastName + " " + firstName + (patronymic != null ? " " + patronymic : " ");
    }

}

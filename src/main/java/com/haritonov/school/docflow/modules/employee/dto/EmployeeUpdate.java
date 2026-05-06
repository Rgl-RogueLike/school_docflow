package com.haritonov.school.docflow.modules.employee.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeUpdate {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phoneNumber;
    private LocalDate dateOfEmployment;
    private Long positionId;
}

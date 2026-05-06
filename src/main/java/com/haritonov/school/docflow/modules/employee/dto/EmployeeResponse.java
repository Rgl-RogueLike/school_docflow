package com.haritonov.school.docflow.modules.employee.dto;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phoneNumber;
    private String positionName;

    public String getFullName() {
        return lastName + " " + firstName + (patronymic != null ? " " + patronymic : " ");
    }
}

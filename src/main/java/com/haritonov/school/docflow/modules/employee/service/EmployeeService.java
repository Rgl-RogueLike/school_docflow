package com.haritonov.school.docflow.modules.employee.service;

import com.haritonov.school.docflow.modules.employee.dto.EmployeeCreateRequest;
import com.haritonov.school.docflow.modules.employee.dto.EmployeeResponse;
import com.haritonov.school.docflow.modules.employee.dto.EmployeeUpdateRequest;

import java.util.List;

public interface EmployeeService {

    Long create(EmployeeCreateRequest request);

    List<EmployeeResponse> getAll();

    EmployeeResponse getById(Long id);

    void update(EmployeeUpdateRequest request);

    void delete(Long id);
}

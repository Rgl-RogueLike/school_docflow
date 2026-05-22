package com.haritonov.school.docflow.modules.user.service;

import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.user.model.AppUser;

public interface CurrentUserService {

    AppUser getCurrentUser();

    Employee getCurrentEmployee();

    Long getCurrentEmployeeId();

    String getCurrentEmployeeFullName();
}

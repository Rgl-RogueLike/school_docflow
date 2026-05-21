package com.haritonov.school.docflow.modules.employee.repository;

import com.haritonov.school.docflow.modules.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

package com.haritonov.school.docflow.modules.employee.service;

import com.haritonov.school.docflow.modules.employee.dto.EmployeeCreateRequest;
import com.haritonov.school.docflow.modules.employee.dto.EmployeeResponse;
import com.haritonov.school.docflow.modules.employee.dto.EmployeeUpdateRequest;
import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.employee.model.Position;
import com.haritonov.school.docflow.modules.employee.repository.EmployeeRepository;
import com.haritonov.school.docflow.modules.employee.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.openmbean.OpenDataException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    private EmployeeResponse mapToResponse(Employee entity) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(entity.getId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setPatronymic(entity.getPatronymic());
        if(entity.getPosition() != null) {
            response.setPositionName(entity.getPosition().getName());
        }
        return response;
    }

    @Override
    @Transactional
    public Long create(EmployeeCreateRequest request) {
        Position position = null;
        if (request.getPositionId() != null) {
            position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new IllegalArgumentException("Должность не найдена"));
        }
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPatronymic(request.getPatronymic());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setPosition(position);
        Employee savedEmployee = employeeRepository.save(employee);
        return savedEmployee.getId();
    }

    @Override
    @Transactional
    public List<EmployeeResponse> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public void update(EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        Position position = null;
        if (request.getPositionId() != null) {
            position = positionRepository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Должность не найдне"));
        }
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPatronymic(request.getPatronymic());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setPosition(position);
        employeeRepository.save(employee);

    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Сотдрудник не найден");
        }
        employeeRepository.deleteById(id);
    }
}

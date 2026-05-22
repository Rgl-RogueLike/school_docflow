package com.haritonov.school.docflow.modules.dislocation.service;

import com.haritonov.school.docflow.modules.dislocation.dto.DislocationCreateRequest;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationFilterDto;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationResponse;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationUpdateRequest;
import com.haritonov.school.docflow.modules.dislocation.model.OrderOfTemporaryDislocation;
import com.haritonov.school.docflow.modules.dislocation.repository.DislocationRepository;
import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.employee.repository.EmployeeRepository;
import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DislocationServiceImpl implements DislocationService{

    private final DislocationRepository dislocationRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    private DislocationResponse mapToResponse(OrderOfTemporaryDislocation entity) {
        DislocationResponse response = new DislocationResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setRelation(entity.getRelation());
        response.setBasis(entity.getBasis());
        response.setDateFrom(entity.getDateFrom());
        response.setEffectiveStartDate(entity.getEffectiveStartDate());
        response.setEffectiveEndDate(entity.getEffectiveEndDate());
        if (entity.getStudent() != null) {
            Student student = entity.getStudent();
            response.setStudentId(student.getId());
            response.setStudentFullName(student.getLastName() + student.getFirstName() +
                    (student.getPatronymic() != null ? student.getPatronymic() : " "));
        }

        if (entity.getCreator() != null) {
            Employee employee = entity.getCreator();
            response.setCreatorFullName(employee.getLastName() + employee.getFirstName() +
                    (employee.getPatronymic() != null ? employee.getPatronymic() : " "));
        }
        return response;
    }

    @Override
    @Transactional
    public Long create(DislocationCreateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        OrderOfTemporaryDislocation order = new OrderOfTemporaryDislocation();
        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setRelation(request.getRelation());
        order.setBasis(request.getBasis());
        order.setDateFrom(request.getDateFrom());
        order.setEffectiveStartDate(request.getEffectiveEndDate());
        order.setStudent(student);
        order.setCreator(employee);
        OrderOfTemporaryDislocation savedOrder = dislocationRepository.save(order);
        return savedOrder.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DislocationResponse> getAll() {
        List<OrderOfTemporaryDislocation> orders = dislocationRepository.findAll();
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DislocationResponse getById(Long id) {
        OrderOfTemporaryDislocation order = dislocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Документ не найден"));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public void update(DislocationUpdateRequest request) {
        OrderOfTemporaryDislocation order = dislocationRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Документ не найден"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setRelation(request.getRelation());
        order.setBasis(request.getBasis());
        order.setDateFrom(request.getDateFrom());
        order.setEffectiveStartDate(request.getEffectiveStartDate());
        order.setEffectiveEndDate(request.getEffectiveEndDate());
        order.setStudent(student);
        order.setCreator(employee);
        dislocationRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        if (!dislocationRepository.existsById(id)) {
            throw new IllegalArgumentException("Документ не найден");
        }
        dislocationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DislocationResponse> getAllWithFilter(DislocationFilterDto filter) {
        List<OrderOfTemporaryDislocation> orders = dislocationRepository.findAll(DislocationRepository.withFilter(filter));
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}

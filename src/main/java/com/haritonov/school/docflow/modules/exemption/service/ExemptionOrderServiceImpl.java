package com.haritonov.school.docflow.modules.exemption.service;

import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderCreateRequest;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderFilterDto;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderResponse;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderUpdateRequest;
import com.haritonov.school.docflow.modules.exemption.model.ExemptionOrder;
import com.haritonov.school.docflow.modules.exemption.model.enums.ExemptionOrderStatus;
import com.haritonov.school.docflow.modules.exemption.repository.ExemptionOrderRepository;
import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.employee.repository.EmployeeRepository;
import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExemptionOrderServiceImpl implements ExemptionOrderService {

    private final ExemptionOrderRepository exemptionRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    private ExemptionOrderResponse mapToResponse(ExemptionOrder entity) {
        ExemptionOrderResponse response = new ExemptionOrderResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setPurpose(entity.getPurpose());
        response.setDateFrom(entity.getDateFrom());
        response.setIssueDate(entity.getIssueDate());
        response.setStatus(calculateStatus(entity));
        if (entity.getStudent() != null) {
            Student student = entity.getStudent();
            response.setStudentId(student.getId());
            response.setStudentFullName(student.getLastName() + " " + student.getFirstName() +
                    (student.getPatronymic() != null ? " " + student.getPatronymic() : " "));
            response.setStudentDateOfBirth(student.getDateOfBirth());
            if (student.getEducationalClass() != null) {
                response.setStudentClassName(student.getEducationalClass().getName());
            }
        }

        if (entity.getCreatedAt() != null) {
            Employee employee = entity.getCreator();
            response.setCreatorFullName(employee.getLastName() + " " + employee.getFirstName() +
                    (employee.getPatronymic() != null ? " " + employee.getPatronymic() : " "));
        }
        return response;
    }

    private ExemptionOrderStatus calculateStatus(ExemptionOrder certificate) {
        LocalDate now = LocalDate.now();
        LocalDate start = certificate.getDateFrom();
        LocalDate end = certificate.getIssueDate();

        if (start == null || end == null) {
            return ExemptionOrderStatus.EXPIRED; // или неизвестно
        }

        if (now.isBefore(start)) {
            return ExemptionOrderStatus.NOT_STARTED;
        } else if (now.isAfter(end)) {
            return ExemptionOrderStatus.EXPIRED;
        } else if (ChronoUnit.DAYS.between(now, end) <= 3) {
            return ExemptionOrderStatus.EXPIRING_SOON;
        } else {
            return ExemptionOrderStatus.ACTIVE;
        }
    }

    @Override
    @Transactional
    public Long create(ExemptionOrderCreateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        ExemptionOrder certificate = new ExemptionOrder();
        certificate.setDocumentNumber(request.getDocumentNumber());
        certificate.setDocumentDate(request.getDocumentDate().atStartOfDay());
        certificate.setPurpose(request.getPurpose());
        certificate.setDateFrom(request.getDateFrom());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setStudent(student);
        certificate.setCreator(employee);
        ExemptionOrder savedCertificate = exemptionRepository.save(certificate);
        return savedCertificate.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExemptionOrderResponse> getAll() {
        List<ExemptionOrder> certificates = exemptionRepository.findAll();
        return certificates.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExemptionOrderResponse getById(Long id) {
        ExemptionOrder certificate = exemptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Справка не найдена"));
        return mapToResponse(certificate);
    }

    @Override
    @Transactional
    public void update(ExemptionOrderUpdateRequest request) {
        ExemptionOrder certificate = exemptionRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Справка не найдена"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        certificate.setDocumentNumber(request.getDocumentNumber());
        certificate.setDocumentDate(request.getDocumentDate().atStartOfDay());
        certificate.setPurpose(request.getPurpose());
        certificate.setDateFrom(request.getDateFrom());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setStudent(student);
        exemptionRepository.save(certificate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!exemptionRepository.existsById(id)) {
            throw new IllegalArgumentException("Справка не найдена");
        }
        exemptionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExemptionOrderResponse> getAllWithFilter(ExemptionOrderFilterDto filter) {
        List<ExemptionOrder> certificates = exemptionRepository.findAll(ExemptionOrderRepository.withFilter(filter));
        return certificates.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}

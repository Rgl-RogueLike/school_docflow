package com.haritonov.school.docflow.modules.certificate.service;

import com.haritonov.school.docflow.modules.certificate.dto.*;
import com.haritonov.school.docflow.modules.certificate.model.Certificate;
import com.haritonov.school.docflow.modules.certificate.repository.CertificateRepository;
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
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    private CertificateResponse mapToResponse(Certificate entity) {
        CertificateResponse response = new CertificateResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setAcademicYear(entity.getAcademicYear());
        if (entity.getStudent() != null) {
            Student student = entity.getStudent();
            response.setStudentId(student.getId());
            response.setStudentFullName(student.getLastName() + " " + student.getFirstName() +
                    (student.getPatronymic() != null ? " " + student.getPatronymic() : ""));
            response.setStudentDateOfBirth(student.getDateOfBirth());
            if (student.getEducationalClass() != null) {
                response.setStudentClassName(student.getEducationalClass().getName());
            }
        }
        if (entity.getCreator() != null) {
            Employee creator = entity.getCreator();
            response.setCreatorFullName(creator.getLastName() + " " + creator.getFirstName() +
                    (creator.getPatronymic() != null ? " " + creator.getPatronymic() : ""));
        }
        return response;
    }

    @Override
    @Transactional
    public Long create(CertificateCreateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        Certificate cert = new Certificate();
        cert.setDocumentNumber(request.getDocumentNumber());
        cert.setDocumentDate(request.getDocumentDate().atStartOfDay());
        cert.setAcademicYear(request.getAcademicYear());
        cert.setStudent(student);
        cert.setCreator(employee);
        Certificate saved = certificateRepository.save(cert);
        return saved.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> getAll() {
        return certificateRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse getById(Long id) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Справка не найдена"));
        return mapToResponse(cert);
    }

    @Override
    @Transactional
    public void update(CertificateUpdateRequest request) {
        Certificate cert = certificateRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Справка не найдена"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        cert.setDocumentNumber(request.getDocumentNumber());
        cert.setDocumentDate(request.getDocumentDate().atStartOfDay());
        cert.setAcademicYear(request.getAcademicYear());
        cert.setStudent(student);
        certificateRepository.save(cert);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!certificateRepository.existsById(id)) {
            throw new IllegalArgumentException("Справка не найдена");
        }
        certificateRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> getAllWithFilter(CertificateFilterDto filter) {
        List<Certificate> certificates = certificateRepository.findAll(CertificateRepository.withFilter(filter));
        return certificates.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
}
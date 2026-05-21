package com.haritonov.school.docflow.modules.certificate.service;

import com.haritonov.school.docflow.modules.certificate.dto.CertificateCreateRequest;
import com.haritonov.school.docflow.modules.certificate.dto.CertificateResponse;
import com.haritonov.school.docflow.modules.certificate.dto.CertificateUpdateRequest;
import com.haritonov.school.docflow.modules.certificate.model.CertificateOfStudy;
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

    private CertificateResponse mapToResponse(CertificateOfStudy entity) {
        CertificateResponse response = new CertificateResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setPurpose(entity.getPurpose());
        response.setDateFrom(entity.getDateFrom());
        response.setIssueDate(entity.getIssueDate());
        if (entity.getStudent() != null) {
            Student student = entity.getStudent();
            response.setStudentFullName(student.getLastName() + " " + student.getFirstName() +
                    (student.getPatronymic() != null ? " " + student.getPatronymic() : " "));
        }

        if (entity.getCreatedAt() != null) {
            Employee employee = entity.getCreator();
            response.setCreatorFullName(employee.getLastName() + " " + employee.getFirstName() +
                    (employee.getPatronymic() != null ? " " + employee.getPatronymic() : " "));
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
        CertificateOfStudy certificate = new CertificateOfStudy();
        certificate.setDocumentNumber(request.getDocumentNumber());
        certificate.setDocumentDate(request.getDocumentDate().atStartOfDay());
        certificate.setPurpose(request.getPurpose());
        certificate.setDateFrom(request.getDateFrom());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setStudent(student);
        certificate.setCreator(employee);
        CertificateOfStudy savedCertificate = certificateRepository.save(certificate);
        return savedCertificate.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> getAll() {
        List<CertificateOfStudy> certificates = certificateRepository.findAll();
        return certificates.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse getById(Long id) {
        CertificateOfStudy certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Справка не найдена"));
        return mapToResponse(certificate);
    }

    @Override
    @Transactional
    public void update(CertificateUpdateRequest request) {
        CertificateOfStudy certificate = certificateRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Справка не найдена"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        certificate.setDocumentNumber(request.getDocumentNumber());
        certificate.setDocumentDate(request.getDocumentDate().atStartOfDay());
        certificate.setPurpose(request.getPurpose());
        certificate.setDateFrom(request.getDateFrom());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setStudent(student);
        certificate.setCreator(employee);
        certificateRepository.save(certificate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!certificateRepository.existsById(id)) {
            throw new IllegalArgumentException("Справка не найдена");
        }
        certificateRepository.deleteById(id);
    }
}

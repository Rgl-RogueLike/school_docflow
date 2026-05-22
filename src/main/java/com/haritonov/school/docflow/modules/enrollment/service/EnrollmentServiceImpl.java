package com.haritonov.school.docflow.modules.enrollment.service;

import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentCreateRequest;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentUpdateRequest;
import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollment;
import com.haritonov.school.docflow.modules.enrollment.repository.EnrollmentRepository;
import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.employee.repository.EmployeeRepository;
import com.haritonov.school.docflow.modules.student.model.EducationalClass;
import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.repository.EducationalClassRepository;
import com.haritonov.school.docflow.modules.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final EducationalClassRepository classRepository;
    private final EmployeeRepository employeeRepository;

    private EnrollmentResponse mapToResponse(OrderOfEnrollment entity) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setEnrollmentDate(entity.getEnrollmentDate());
        response.setReason(entity.getReason());

        if (entity.getStudent() != null) {
            Student student = entity.getStudent();
            response.setStudentId(student.getId());
            response.setLastName(student.getLastName());
            response.setFirstName(student.getFirstName());
            response.setPatronymic(student.getPatronymic());
            response.setGender(student.getGender());
            response.setDateOfBirth(student.getDateOfBirth());
            response.setStudentFullName(student.getLastName() + " " + student.getFirstName() +
                    (student.getPatronymic() != null ? " " + student.getPatronymic() : ""));

            if (student.getEducationalClass() != null) {
                response.setClassId(student.getEducationalClass().getId());
                response.setClassName(student.getEducationalClass().getName());
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
    public Long create(EnrollmentCreateRequest request) {
        EducationalClass educationalClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));

        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPatronymic(request.getPatronymic());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setDateOfEnrollment(request.getEnrollmentDate());
        student.setEducationalClass(educationalClass);

        Student savedStudent = studentRepository.save(student);

        OrderOfEnrollment order = new OrderOfEnrollment();
        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setEnrollmentDate(request.getEnrollmentDate());
        order.setReason(request.getReason());
        order.setStudent(savedStudent);
        order.setCreator(employee);

        OrderOfEnrollment savedOrder = enrollmentRepository.save(order);
        return savedOrder.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {
        OrderOfEnrollment order = enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public void update(EnrollmentUpdateRequest request) {
        OrderOfEnrollment order = enrollmentRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));

        EducationalClass educationalClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));

        Student student = order.getStudent();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPatronymic(request.getPatronymic());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setEducationalClass(educationalClass);
        student.setDateOfEnrollment(request.getEnrollmentDate());
        studentRepository.save(student);

        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setEnrollmentDate(request.getEnrollmentDate());
        order.setReason(request.getReason());
        order.setStudent(student);

        enrollmentRepository.save(order);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        OrderOfEnrollment order = enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));
        Student student = order.getStudent();
        enrollmentRepository.delete(order);
        if (student != null) {
            studentRepository.delete(student);
        }
    }
}
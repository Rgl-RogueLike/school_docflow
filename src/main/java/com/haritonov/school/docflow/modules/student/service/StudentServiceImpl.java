package com.haritonov.school.docflow.modules.student.service;

import com.haritonov.school.docflow.modules.student.dto.StudentCreateRequest;
import com.haritonov.school.docflow.modules.student.dto.StudentFilterDto;
import com.haritonov.school.docflow.modules.student.dto.StudentResponse;
import com.haritonov.school.docflow.modules.student.dto.StudentUpdateRequest;
import com.haritonov.school.docflow.modules.student.model.EducationalClass;
import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.repository.EducationalClassRepository;
import com.haritonov.school.docflow.modules.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final EducationalClassRepository educationalClassRepository;

    private StudentResponse mapToResponse(Student entity) {
        StudentResponse response = new StudentResponse();
        response.setId(entity.getId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setPatronymic(entity.getPatronymic());
        response.setGender(entity.getGender());
        response.setDateOfBirth(entity.getDateOfBirth());
        if(entity.getEducationalClass() != null) {
            response.setClassName(entity.getEducationalClass().getName());
        }
        return response;
    }

    @Override
    @Transactional
    public Long create(StudentCreateRequest request) {
        EducationalClass educationalClass = educationalClassRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPatronymic(request.getPatronymic());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setDateOfEnrollment(request.getDateOfEnrollment());
        student.setEducationalClass(educationalClass);
        Student savedStudent = studentRepository.save(student);
        return savedStudent.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        return mapToResponse(student);
    }

    @Override
    @Transactional
    public void update(StudentUpdateRequest request) {
        Student student = studentRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
        EducationalClass educationalClass = null;
        if (request.getClassId() != null) {
            educationalClass = educationalClassRepository.findById(request.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));
        }
        LocalDate existingDateOfEnrollment = student.getDateOfEnrollment();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPatronymic(request.getPatronymic());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setDateOfEnrollment(existingDateOfEnrollment);
        student.setEducationalClass(educationalClass);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Ученик не найден");
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getByClassId(Long classId) {
        List<Student> students = studentRepository.findByEducationalClass_Id(classId);
        return students.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllWithFilter(StudentFilterDto filter) {
        List<Student> students = studentRepository.findAll(StudentRepository.withFilter(filter));
        return students.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}

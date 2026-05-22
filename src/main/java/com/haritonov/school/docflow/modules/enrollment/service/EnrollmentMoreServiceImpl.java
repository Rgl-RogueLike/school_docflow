package com.haritonov.school.docflow.modules.enrollment.service;

import com.haritonov.school.docflow.modules.enrollment.dto.*;
import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollmentMore;
import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollmentMoreItem;
import com.haritonov.school.docflow.modules.enrollment.repository.EnrollmentMoreItemRepository;
import com.haritonov.school.docflow.modules.enrollment.repository.EnrollmentMoreRepository;
import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.employee.repository.EmployeeRepository;
import com.haritonov.school.docflow.modules.student.model.EducationalClass;
import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.repository.EducationalClassRepository;
import com.haritonov.school.docflow.modules.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentMoreServiceImpl implements EnrollmentMoreService {

    private final EnrollmentMoreRepository enrollmentMoreRepository;
    private final EnrollmentMoreItemRepository itemRepository;
    private final StudentRepository studentRepository;
    private final EducationalClassRepository classRepository;
    private final EmployeeRepository employeeRepository;

    private EnrollmentMoreResponse mapToResponse(OrderOfEnrollmentMore entity) {
        EnrollmentMoreResponse response = new EnrollmentMoreResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setBasis(entity.getBasis());
        response.setDateFrom(entity.getDateFrom());

        if (entity.getCreator() != null) {
            Employee creator = entity.getCreator();
            response.setCreatorFullName(creator.getLastName() + " " + creator.getFirstName() +
                    (creator.getPatronymic() != null ? " " + creator.getPatronymic() : ""));
        }

        List<EnrollmentMoreItemResponse> itemResponses = entity.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        return response;
    }

    private EnrollmentMoreItemResponse mapItemToResponse(OrderOfEnrollmentMoreItem item) {
        EnrollmentMoreItemResponse response = new EnrollmentMoreItemResponse();
        response.setId(item.getId());

        if (item.getStudent() != null) {
            Student student = item.getStudent();
            response.setStudentId(student.getId());
            response.setLastName(student.getLastName());
            response.setFirstName(student.getFirstName());
            response.setPatronymic(student.getPatronymic());
            response.setGender(student.getGender());
            response.setDateOfBirth(student.getDateOfBirth());
            response.setEnrollmentDate(student.getDateOfEnrollment());
            response.setStudentFullName(student.getLastName() + " " + student.getFirstName() +
                    (student.getPatronymic() != null ? " " + student.getPatronymic() : ""));
        }

        return response;
    }

    @Override
    @Transactional
    public Long create(EnrollmentMoreCreateRequest request) {
        EducationalClass educationalClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));

        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));

        OrderOfEnrollmentMore order = new OrderOfEnrollmentMore();
        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setBasis(request.getBasis());
        order.setDateFrom(request.getDateFrom());
        order.setCreator(employee);

        for (EnrollmentMoreItemDto itemDto : request.getItems()) {
            Student student = new Student();
            student.setLastName(itemDto.getLastName());
            student.setFirstName(itemDto.getFirstName());
            student.setPatronymic(itemDto.getPatronymic());
            student.setGender(itemDto.getGender());
            student.setDateOfBirth(itemDto.getDateOfBirth());
            student.setDateOfEnrollment(itemDto.getEnrollmentDate());
            student.setEducationalClass(educationalClass);
            Student savedStudent = studentRepository.save(student);

            OrderOfEnrollmentMoreItem item = new OrderOfEnrollmentMoreItem();
            item.setStudent(savedStudent);
            item.setOrder(order);
            order.addItem(item);
        }

        OrderOfEnrollmentMore savedOrder = enrollmentMoreRepository.save(order);
        return savedOrder.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentMoreResponse> getAll() {
        return enrollmentMoreRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentMoreResponse getById(Long id) {
        OrderOfEnrollmentMore order = enrollmentMoreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public void update(EnrollmentMoreUpdateRequest request) {
        OrderOfEnrollmentMore order = enrollmentMoreRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));

        EducationalClass educationalClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Класс не найден"));

        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setBasis(request.getBasis());
        order.setDateFrom(request.getDateFrom());

        List<Long> requestedStudentIds = request.getItems().stream()
                .filter(item -> item.getId() != null)
                .map(EnrollmentMoreItemDto::getId)
                .collect(Collectors.toList());

        List<OrderOfEnrollmentMoreItem> itemsToRemove = order.getItems().stream()
                .filter(item -> !requestedStudentIds.contains(item.getStudent().getId()))
                .collect(Collectors.toList());

        for (OrderOfEnrollmentMoreItem itemToRemove : itemsToRemove) {
            order.removeItem(itemToRemove);
            itemRepository.delete(itemToRemove);

            if (itemToRemove.getStudent() != null) {
                studentRepository.delete(itemToRemove.getStudent());
            }
        }

        for (EnrollmentMoreItemDto itemDto : request.getItems()) {
            if (itemDto.getId() != null) {
                Student existingStudent = studentRepository.findById(itemDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));
                existingStudent.setLastName(itemDto.getLastName());
                existingStudent.setFirstName(itemDto.getFirstName());
                existingStudent.setPatronymic(itemDto.getPatronymic());
                existingStudent.setGender(itemDto.getGender());
                existingStudent.setDateOfBirth(itemDto.getDateOfBirth());
                existingStudent.setDateOfEnrollment(itemDto.getEnrollmentDate());
                existingStudent.setEducationalClass(educationalClass);
                studentRepository.save(existingStudent);

                OrderOfEnrollmentMoreItem existingItem = order.getItems().stream()
                        .filter(item -> item.getStudent().getId().equals(existingStudent.getId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem == null) {
                    OrderOfEnrollmentMoreItem newItem = new OrderOfEnrollmentMoreItem();
                    newItem.setStudent(existingStudent);
                    newItem.setOrder(order);
                    order.addItem(newItem);
                }
            } else {
                Student newStudent = new Student();
                newStudent.setLastName(itemDto.getLastName());
                newStudent.setFirstName(itemDto.getFirstName());
                newStudent.setPatronymic(itemDto.getPatronymic());
                newStudent.setGender(itemDto.getGender());
                newStudent.setDateOfBirth(itemDto.getDateOfBirth());
                newStudent.setDateOfEnrollment(itemDto.getEnrollmentDate());
                newStudent.setEducationalClass(educationalClass);
                newStudent = studentRepository.save(newStudent);

                OrderOfEnrollmentMoreItem newItem = new OrderOfEnrollmentMoreItem();
                newItem.setStudent(newStudent);
                newItem.setOrder(order);
                order.addItem(newItem);
            }
        }

        enrollmentMoreRepository.save(order);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        OrderOfEnrollmentMore order = enrollmentMoreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));

        List<Student> studentsToDelete = order.getItems().stream()
                .map(OrderOfEnrollmentMoreItem::getStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        enrollmentMoreRepository.delete(order);

        for (Student student : studentsToDelete) {
            studentRepository.delete(student);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentMoreResponse> getAllWithFilter(EnrollmentMoreFilterDto filter) {
        List<OrderOfEnrollmentMore> orders = enrollmentMoreRepository.findAll(EnrollmentMoreRepository.withFilter(filter));
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
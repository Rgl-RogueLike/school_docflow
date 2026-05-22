package com.haritonov.school.docflow.modules.release.service;

import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.employee.repository.EmployeeRepository;
import com.haritonov.school.docflow.modules.release.dto.ReleaseCreateRequest;
import com.haritonov.school.docflow.modules.release.dto.ReleaseFilterDto;
import com.haritonov.school.docflow.modules.release.dto.ReleaseResponse;
import com.haritonov.school.docflow.modules.release.dto.ReleaseUpdateRequest;
import com.haritonov.school.docflow.modules.release.model.OrderOfReleaseStudent;
import com.haritonov.school.docflow.modules.release.repository.ReleaseRepository;
import com.haritonov.school.docflow.modules.student.model.Student;
import com.haritonov.school.docflow.modules.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    private ReleaseResponse mapToResponse(OrderOfReleaseStudent entity) {
        ReleaseResponse response = new ReleaseResponse();
        response.setId(entity.getId());
        response.setDocumentNumber(entity.getDocumentNumber());
        response.setDocumentDate(entity.getDocumentDate());
        response.setBasis(entity.getBasis());
        response.setBasisDocumentDate(entity.getBasisDocumentDate());
        response.setDateFrom(entity.getDateFrom());
        response.setDateOn(entity.getDateOn());

        if (entity.getStudent() != null) {
            Student student = entity.getStudent();
            response.setStudentId(student.getId());
            response.setStudentFullName(student.getLastName() + " " + student.getFirstName() +
                    (student.getPatronymic() != null ? " " + student.getPatronymic() : ""));
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
    public Long create(ReleaseCreateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));

        Employee employee = employeeRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));

        OrderOfReleaseStudent order = new OrderOfReleaseStudent();
        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setBasis(request.getBasis());
        order.setBasisDocumentDate(request.getBasisDocumentDate());
        order.setDateFrom(request.getDateFrom());
        order.setDateOn(request.getDateOn());
        order.setStudent(student);
        order.setCreator(employee);

        OrderOfReleaseStudent savedOrder = releaseRepository.save(order);
        return savedOrder.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponse> getAll() {
        return releaseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseResponse getById(Long id) {
        OrderOfReleaseStudent order = releaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public void update(ReleaseUpdateRequest request) {
        OrderOfReleaseStudent order = releaseRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Приказ не найден"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден"));

        order.setDocumentNumber(request.getDocumentNumber());
        order.setDocumentDate(request.getDocumentDate().atStartOfDay());
        order.setBasis(request.getBasis());
        order.setBasisDocumentDate(request.getBasisDocumentDate());
        order.setDateFrom(request.getDateFrom());
        order.setDateOn(request.getDateOn());
        order.setStudent(student);

        releaseRepository.save(order);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!releaseRepository.existsById(id)) {
            throw new IllegalArgumentException("Приказ не найден");
        }
        releaseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponse> getAllWithFilter(ReleaseFilterDto filter) {
        List<OrderOfReleaseStudent> orders = releaseRepository.findAll(ReleaseRepository.withFilter(filter));
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
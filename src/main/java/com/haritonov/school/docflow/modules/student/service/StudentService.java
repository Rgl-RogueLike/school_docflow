package com.haritonov.school.docflow.modules.student.service;

import com.haritonov.school.docflow.modules.student.dto.StudentCreateRequest;
import com.haritonov.school.docflow.modules.student.dto.StudentFilterDto;
import com.haritonov.school.docflow.modules.student.dto.StudentResponse;
import com.haritonov.school.docflow.modules.student.dto.StudentUpdateRequest;

import java.util.List;

public interface StudentService {

    Long create(StudentCreateRequest request);

    List<StudentResponse> getAll();

    StudentResponse getById(Long id);

    void update(StudentUpdateRequest request);

    void delete(Long id);

    List<StudentResponse> getByClassId(Long classId);

    List<StudentResponse> getAllWithFilter(StudentFilterDto filter);
}

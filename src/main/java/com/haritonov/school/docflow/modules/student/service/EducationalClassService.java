package com.haritonov.school.docflow.modules.student.service;

import com.haritonov.school.docflow.modules.student.dto.EducationalClassCreateRequest;
import com.haritonov.school.docflow.modules.student.dto.EducationalClassResponse;
import com.haritonov.school.docflow.modules.student.dto.EducationalClassUpdateRequest;

import java.util.List;

public interface EducationalClassService {

    Long create(EducationalClassCreateRequest request);

    List<EducationalClassResponse> getAll();

    EducationalClassResponse getById(Long id);

    void update(EducationalClassUpdateRequest request);

    void delete(Long id);
}

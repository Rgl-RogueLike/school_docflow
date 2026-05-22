package com.haritonov.school.docflow.modules.enrollment.service;

import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentCreateRequest;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentFilterDto;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentUpdateRequest;

import java.util.List;

public interface EnrollmentService {

    Long create(EnrollmentCreateRequest request);

    List<EnrollmentResponse> getAll();

    EnrollmentResponse getById(Long id);

    void update(EnrollmentUpdateRequest request);

    void delete(Long id);

    List<EnrollmentResponse> getAllWithFilter(EnrollmentFilterDto filter);
}
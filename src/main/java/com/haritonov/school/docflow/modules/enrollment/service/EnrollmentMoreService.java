package com.haritonov.school.docflow.modules.enrollment.service;

import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreCreateRequest;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreFilterDto;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreUpdateRequest;

import java.util.List;

public interface EnrollmentMoreService {

    Long create(EnrollmentMoreCreateRequest request);

    List<EnrollmentMoreResponse> getAll();

    EnrollmentMoreResponse getById(Long id);

    void update(EnrollmentMoreUpdateRequest request);

    void delete(Long id);

    List<EnrollmentMoreResponse> getAllWithFilter(EnrollmentMoreFilterDto filter);
}
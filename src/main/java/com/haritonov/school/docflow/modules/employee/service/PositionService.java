package com.haritonov.school.docflow.modules.employee.service;

import com.haritonov.school.docflow.modules.employee.dto.PositionCreateRequest;
import com.haritonov.school.docflow.modules.employee.dto.PositionResponse;
import com.haritonov.school.docflow.modules.employee.dto.PositionUpdateRequest;

import java.util.List;

public interface PositionService {

    Long create(PositionCreateRequest request);

    List<PositionResponse> getAll();

    PositionResponse getById(Long id);

    void update(PositionUpdateRequest request);

    void delete(Long id);
}

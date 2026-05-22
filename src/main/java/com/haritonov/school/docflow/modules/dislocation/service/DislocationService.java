package com.haritonov.school.docflow.modules.dislocation.service;

import com.haritonov.school.docflow.modules.dislocation.dto.DislocationCreateRequest;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationFilterDto;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationResponse;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationUpdateRequest;

import java.util.List;

public interface DislocationService {

    Long create(DislocationCreateRequest request);

    List<DislocationResponse> getAll();

    DislocationResponse getById(Long id);

    void update(DislocationUpdateRequest request);

    void delete(Long id);

    List<DislocationResponse> getAllWithFilter(DislocationFilterDto filter);
}

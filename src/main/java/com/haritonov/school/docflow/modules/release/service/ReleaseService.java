package com.haritonov.school.docflow.modules.release.service;

import com.haritonov.school.docflow.modules.release.dto.ReleaseCreateRequest;
import com.haritonov.school.docflow.modules.release.dto.ReleaseResponse;
import com.haritonov.school.docflow.modules.release.dto.ReleaseUpdateRequest;

import java.util.List;

public interface ReleaseService {

    Long create(ReleaseCreateRequest request);

    List<ReleaseResponse> getAll();

    ReleaseResponse getById(Long id);

    void update(ReleaseUpdateRequest request);

    void delete(Long id);
}
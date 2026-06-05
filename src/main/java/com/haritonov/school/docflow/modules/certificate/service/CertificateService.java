package com.haritonov.school.docflow.modules.certificate.service;

import com.haritonov.school.docflow.modules.certificate.dto.*;

import java.util.List;

public interface CertificateService {
    Long create(CertificateCreateRequest request);
    List<CertificateResponse> getAll();
    CertificateResponse getById(Long id);
    void update(CertificateUpdateRequest request);
    void delete(Long id);
    List<CertificateResponse> getAllWithFilter(CertificateFilterDto filter);
}
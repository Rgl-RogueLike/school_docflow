package com.haritonov.school.docflow.modules.certificate.service;

import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderCreateRequest;
import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderFilterDto;
import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderResponse;
import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderUpdateRequest;

import java.util.List;

public interface ExemptionOrderService {

    Long create(ExemptionOrderCreateRequest request);

    List<ExemptionOrderResponse> getAll();

    ExemptionOrderResponse getById(Long id);

    void update(ExemptionOrderUpdateRequest request);

    void delete(Long id);

    List<ExemptionOrderResponse> getAllWithFilter(ExemptionOrderFilterDto filter);
}

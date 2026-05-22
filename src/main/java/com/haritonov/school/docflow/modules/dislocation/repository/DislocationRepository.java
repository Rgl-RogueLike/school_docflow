package com.haritonov.school.docflow.modules.dislocation.repository;

import com.haritonov.school.docflow.modules.dislocation.model.OrderOfTemporaryDislocation;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationFilterDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DislocationRepository extends JpaRepository<OrderOfTemporaryDislocation, Long>, JpaSpecificationExecutor<OrderOfTemporaryDislocation> {

    static Specification<OrderOfTemporaryDislocation> withFilter(DislocationFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(filter.getDocumentNumber())) {
                predicates.add(cb.like(root.get("documentNumber"), "%" + filter.getDocumentNumber() + "%"));
            }

            if (filter.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("documentDate"), filter.getDateFrom().atStartOfDay()));
            }

            if (filter.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("documentDate"), filter.getDateTo().atTime(23, 59, 59)));
            }

            if (filter.getEffectiveStartDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveStartDate"), filter.getEffectiveStartDateFrom()));
            }

            if (filter.getEffectiveStartDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("effectiveStartDate"), filter.getEffectiveStartDateTo()));
            }

            if (filter.getEffectiveEndDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveEndDate"), filter.getEffectiveEndDateFrom()));
            }

            if (filter.getEffectiveEndDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("effectiveEndDate"), filter.getEffectiveEndDateTo()));
            }

            if (StringUtils.isNotBlank(filter.getStudentName())) {
                predicates.add(cb.like(
                        cb.concat(cb.concat(root.get("student").get("lastName"), " "),
                                cb.concat(root.get("student").get("firstName"), " ")),
                        "%" + filter.getStudentName() + "%"
                ));
            }

            if (StringUtils.isNotBlank(filter.getCreatorName())) {
                predicates.add(cb.like(
                        cb.concat(cb.concat(root.get("creator").get("lastName"), " "),
                                cb.concat(root.get("creator").get("firstName"), " ")),
                        "%" + filter.getCreatorName() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
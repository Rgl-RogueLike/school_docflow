package com.haritonov.school.docflow.modules.enrollment.repository;

import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollmentMore;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreFilterDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface EnrollmentMoreRepository extends JpaRepository<OrderOfEnrollmentMore, Long>, JpaSpecificationExecutor<OrderOfEnrollmentMore> {

    static Specification<OrderOfEnrollmentMore> withFilter(EnrollmentMoreFilterDto filter) {
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

            if (filter.getEffectiveDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateFrom"), filter.getEffectiveDateFrom()));
            }

            if (filter.getEffectiveDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateFrom"), filter.getEffectiveDateTo()));
            }

            if (StringUtils.isNotBlank(filter.getClassName())) {
                query.distinct(true);
                predicates.add(cb.like(
                        root.join("items").get("student").get("educationalClass").get("name"),
                        "%" + filter.getClassName() + "%"
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
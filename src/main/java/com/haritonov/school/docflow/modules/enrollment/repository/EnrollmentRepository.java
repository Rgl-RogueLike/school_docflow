package com.haritonov.school.docflow.modules.enrollment.repository;

import com.haritonov.school.docflow.modules.enrollment.model.OrderOfEnrollment;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentFilterDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<OrderOfEnrollment, Long>, JpaSpecificationExecutor<OrderOfEnrollment> {

    static Specification<OrderOfEnrollment> withFilter(EnrollmentFilterDto filter) {
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

            if (filter.getEnrollmentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("enrollmentDate"), filter.getEnrollmentDateFrom()));
            }

            if (filter.getEnrollmentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("enrollmentDate"), filter.getEnrollmentDateTo()));
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

            if (StringUtils.isNotBlank(filter.getClassName())) {
                predicates.add(cb.like(root.get("student").get("educationalClass").get("name"), "%" + filter.getClassName() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package com.haritonov.school.docflow.modules.release.repository;

import com.haritonov.school.docflow.modules.release.model.OrderOfReleaseStudent;
import com.haritonov.school.docflow.modules.release.dto.ReleaseFilterDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<OrderOfReleaseStudent, Long>, JpaSpecificationExecutor<OrderOfReleaseStudent> {

    static Specification<OrderOfReleaseStudent> withFilter(ReleaseFilterDto filter) {
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

            if (filter.getReleaseDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOn"), filter.getReleaseDateFrom()));
            }

            if (filter.getReleaseDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateOn"), filter.getReleaseDateTo()));
            }

            if (filter.getBasisDocumentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("basisDocumentDate"), filter.getBasisDocumentDateFrom()));
            }

            if (filter.getBasisDocumentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("basisDocumentDate"), filter.getBasisDocumentDateTo()));
            }

            if (StringUtils.isNotBlank(filter.getStudentName())) {
                predicates.add(cb.like(
                        cb.concat(cb.concat(root.get("student").get("lastName"), " "),
                                cb.concat(root.get("student").get("firstName"), " ")),
                        "%" + filter.getStudentName() + "%"
                ));
            }

            if (StringUtils.isNotBlank(filter.getClassName())) {
                predicates.add(cb.like(root.get("student").get("educationalClass").get("name"), "%" + filter.getClassName() + "%"));
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